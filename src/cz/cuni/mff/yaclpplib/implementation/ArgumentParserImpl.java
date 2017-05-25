package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.*;
import cz.cuni.mff.yaclpplib.annotation.*;
import cz.cuni.mff.yaclpplib.implementation.drivers.DriverLocator;
import cz.cuni.mff.yaclpplib.implementation.options.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * <p>Main implementation of ArgumentParser. </p>
 *
 * <p>It takes care of creating all needed internal classes,
 * calling methods, adding option handlers, forwarding option
 * tokens, calling after parse methods, etc. </p>
 *
 * <p>Visit {@link ArgumentParser} for public API, or feel free
 * to dig into the code to check the actual implementation.</p>
 */
public class ArgumentParserImpl implements ArgumentParser {

    /**
     * List of all options instances added with {@link ArgumentParser#addOptions(Options)} method.
     */
    private final List<Options> definitions = new ArrayList<>();

    /**
     * A manager taking care of {@link Mandatory} annotated fields.
     */
    private final MandatoryManager mandatoryManager = new MandatoryManager();

    /**
     * Maps the options (such as "-s") to their handlers.
     */
    private final Map<String, OptionHandler> optionHandlerMap = new HashMap<>();
    /**
     * A list of handlers. The issue with {@code optionHandlerMap} is that it can
     * contain every handler multiple times.
     */
    private final List<OptionHandler> optionHandlerList = new ArrayList<>();
    /**
     * A list of {@link AfterParse} annotated methods.
     */
    private final List<ParserEventHandler> afterParseMethods = new ArrayList<>();
    /**
     * A list of {@link AfterParse} annotated methods.
     */
    private final List<ParserEventHandler> beforeParseMethods = new ArrayList<>();

    /**
     * A locator which provides drivers for parsing different types of arguments.
     */
    private DriverLocator driverLocator;

    /**
     * Default handler for unexpected (positional) arguments.
     */
    private UnexpectedParameterHandler unexpectedParameterHandler = value -> {
        throw new UnhandledArgumentException(value);
    };

    @Override
    public <T extends Options> T addOptions(T instance) {
        definitions.add(instance);
        addFieldOptions(instance);
        addMethodOptions(instance);
        addEventHandlerMethods(instance, AfterParse.class, afterParseMethods);
        addEventHandlerMethods(instance, BeforeParse.class, beforeParseMethods);
        addCompositedOptions(instance);
        return instance;
    }

    /**
     * Process all fields annotated with {@link Option}, and add their handlers.
     * @param options the options class being processed
     */
    private <T extends Options> void addFieldOptions(T options) {
        for (Field field : options.getClass().getDeclaredFields()) {
            if (field.getDeclaredAnnotationsByType(Option.class).length > 0) {
                addHandler(new FieldOption(this, options, field), field);
            }
        }
    }

    /**
     * Recursively add composited options.
     * @param instance an instance of Options possibly containing other Options
     */
    private <T extends Options> void addCompositedOptions(T instance) {
        for (Field field : instance.getClass().getDeclaredFields()) {
            if (Options.class.isAssignableFrom(field.getType())) {
                SecurityUtility.makeAccessible(field);
                try {
                    Options composited = (Options) field.get(instance);
                    /* Check if we're not adding an already added instance.
                     * Happens always, because Java objects contain "this" as a field.
                     * Also, checking if composited == instance is not sufficient
                     * because of inner classes (having reference to upper class)
                     * and cross-linked options.
                     *
                     * As we have only one instance of each class, it's OK
                     * to iterate through list, no need to use sets.
                     * But we need to compare references, not equality.
                     */
                    if (definitions.stream().noneMatch((x) -> x == composited))
                        addOptions(composited);
                } catch (IllegalAccessException e) {
                    throw new InternalError("The SecurityUtility shall fail first.");
                }
            }
        }
    }

    /**
     * Process all methods annotated with {@link Option}, and add their handlers.
     * @param options the options class being processed
     */
    private <T extends Options> void addMethodOptions(T options) {
        for (Method method : options.getClass().getDeclaredMethods()) {
            if (method.getDeclaredAnnotationsByType(Option.class).length > 0) {
                addHandler(new MethodOption(this, options, method), method);
            }
        }
    }

    /**
     * Process all methods annotated with {@link AfterParse}, and add them to the afterParseMethods list.
     * @param options the options class being processed
     */
    private <T extends Options> void addEventHandlerMethods(T options, Class annotationClass, List<ParserEventHandler> handlerList) {
        for (Method method : options.getClass().getDeclaredMethods()) {
            if (method.getDeclaredAnnotation(annotationClass) != null) {
                handlerList.add(new ParserEventHandler(method, options));
            }
        }
    }

    /**
     * Add new option - a field or method.
     * @param rawHandler a "plain" handler of field or method
     * @param member the field or method in question
     */
    private void addHandler(OptionHandler rawHandler, AccessibleObject member) {
        // Handle all the semantic quirks
        final OptionHandler handler = wrapHandler(rawHandler, member);

        if (!driverLocator.hasDriverFor(handler.getType()))
            throw new InvalidSetupError("Sorry, the type " + handler.getType().getTypeName() + " cannot be parsed.");

        // Register this option for mandatory checking
        mandatoryManager.add(handler, member);

        // Add it to the list of all handlers
        optionHandlerList.add(handler);

        // Add all @Option synonyms to the map
        Arrays.stream(member.getDeclaredAnnotationsByType(Option.class))
                .forEach(option -> mapOption(option.value(), handler));
    }

    /**
     * Add one @Option - concrete alias.
     * @param alias "-s" or "--long" alias
     * @param handler the handler for this alias
     */
    private void mapOption(String alias, OptionHandler handler) {
        if (optionHandlerMap.containsKey(alias)) {
            throw new InvalidSetupError("One option (" + alias + ") can't be used at multiple methods or fields.");
        }
        optionHandlerMap.put(alias, handler);
    }

    /**
     * <p>Some options have different semantics. Because these generate too much combinations,
     * we handle them using decorators over the handlers. </p>
     *
     * <p>For now, we handle: </p>
     * <ul>
     *     <li> arrays, by aggregating the component type, yielding the final array at the end</li>
     *     <li> primitive types, because they often require special handling</li>
     *     <li> boolean options, because --verbose is a shorthand for "--verbose true"</li>
     *     <li> range options, we need to check if the value is in the given range</li>
     * </ul>
     * <p>Please note that the <b>order is important</b> here, as it defines the final semantics.</p>
     *
     * @param rawHandler a handler to be wrapped
     * @return handler, wrapped if applicable
     */
    private static OptionHandler wrapHandler(OptionHandler rawHandler, AccessibleObject member) {
        OptionHandler wrappedHandler = rawHandler;

        // One-dimensional arrays of known types
        wrappedHandler = ArrayOption.wrapIfApplicable(wrappedHandler);

        // Autoboxer for types
        wrappedHandler = BoxedOption.wrapIfApplicable(wrappedHandler);

        // Allows optional value for booleans
        wrappedHandler = BooleanOption.wrapIfApplicable(wrappedHandler);

        // Does range checks on integers/longs
        wrappedHandler = RangeOption.wrapIfApplicable(wrappedHandler, member);

        return wrappedHandler;
    }

    /**
     * Returns the driver locator parser currently uses.
     * @return used driver locator
     */
    public DriverLocator getDriverLocator() {
        return driverLocator;
    }

    /**
     * Sets the driver locator to given locator.
     *
     * @param driverLocator new driver locator
     */
    public void setDriverLocator(DriverLocator driverLocator) {
        this.driverLocator = driverLocator;

        for (OptionHandler optionHandler : optionHandlerList) {
            if (!driverLocator.hasDriverFor(optionHandler.getType()))
                throw new InvalidSetupError("The new Driver Locator cannot handle "
                        + optionHandler.getType().getTypeName()
                        + ", which is required for option "
                        + optionHandler.getAnyOptionName());
        }

    }

    @Override
    public void parse(String[] args) throws UnhandledArgumentException {
        // Create a queue from the tokens
        final TokenList tokenList = new TokenList(args);

        callEventHandlers(beforeParseMethods);

        while (tokenList.size() > 0) {
            final String optionToken = tokenList.remove();

            if (optionToken.equals("--")) {
                // The delimiter, rest are positional arguments
                while (tokenList.size() > 0) {
                    unexpectedParameterHandler.handle(tokenList.remove());
                }
                break;
            }

            // Try to create an option value from the token
            final Optional<InternalOptionValue> maybeOptionValue =
                    InternalOptionValueFactory.tryCreate(optionToken);

            // We didn't find a match, therefore it's a plain argument possibly
            if (!maybeOptionValue.isPresent()
             || !optionHandlerMap.containsKey(maybeOptionValue.get().getName())) {
                unexpectedParameterHandler.handle(optionToken);
                continue;
            }

            // We have an option value now, complete the instances
            final InternalOptionValue optionValue = maybeOptionValue.get();
            final OptionHandler handler = optionHandlerMap.get(optionValue.getName());

            final ValuePolicy valuePolicy = handler.getValuePolicy();

            // It can have a second part, add it if needed
            if (tokenList.size() > 0) {
                optionValue.completeValue(tokenList, valuePolicy);
            }

            mandatoryManager.encountered(handler);

            // Let's try to find a driver to parse it
            final Class<?> type = handler.getType();

            // Invalid cases
            if (valuePolicy == ValuePolicy.MANDATORY && !optionValue.hasValue()) {
                throw new MissingOptionValue(optionValue);
            }
            if (valuePolicy == ValuePolicy.NEVER && optionValue.hasValue()) {
                throw new InvalidOptionValue("Parameter " + optionValue.getName() + " cannot have an associated value.");
            }

            // Do the parsing
            final Object typedValue = optionValue.hasValue()
                                        ? driverLocator.getDriverFor(type).parse(optionValue)
                                        : null;
            handler.setValue(typedValue, optionValue.getName());
        }

        // Finish all the option arrays we were filling
        for (OptionHandler handler : optionHandlerList) {
            handler.finish();
        }

        // Check if all mandatory options were present
        mandatoryManager.check();
        callEventHandlers(afterParseMethods);
    }

    private void callEventHandlers(List<ParserEventHandler> handlers) {
        // Call all @AfterParse methods
        for (ParserEventHandler afterParse : handlers) {
            afterParse.invoke(this);
        }
    }

    @Override
    public void setUnexpectedParameterHandler(UnexpectedParameterHandler handler) {
        unexpectedParameterHandler = handler;
    }

    @Override
    public String getHelp() {
        StringBuilder builder = new StringBuilder();
        final String endl = System.getProperty("line.separator");

        for (Options options : definitions) {
            // Summary for the help classes
            Help annotation = options.getClass().getDeclaredAnnotation(Help.class);
            if (annotation != null) {
                builder.append(annotation.value()).append(endl);
            }

            // Now deal with every @Option encountered
            for (OptionHandler handler : optionHandlerList) {
                if (handler.getDefinitionClass() != options) {
                    continue;
                }

                builder.append(handler.getHelpLine()).append(endl);
            }
            builder.append(endl);
        }
        return builder.toString();
    }

    @Override
    public List<String> requestPositionalArguments() {
        final ArrayList<String> positionalArgumentsList = new ArrayList<>();
        setUnexpectedParameterHandler(positionalArgumentsList::add);
        return positionalArgumentsList;
    }
}
