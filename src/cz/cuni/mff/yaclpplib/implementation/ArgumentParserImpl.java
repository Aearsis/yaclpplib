package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.*;
import cz.cuni.mff.yaclpplib.annotation.*;

import java.lang.reflect.*;
import java.util.*;

public class ArgumentParserImpl implements ArgumentParser {

    private final List<Options> definitions = new ArrayList<>();

    private final MandatoryManager mandatoryManager = new MandatoryManager();

    private final Map<String, OptionHandler> optionHandlerMap = new HashMap<>();
    private final List<OptionHandler> optionHandlerList = new ArrayList<>();
    private final List<AfterParseHandler> afterParseMethods = new ArrayList<>();

    private DriverCache driverLocator;

    private UnexpectedParameterHandler unexpectedParameterHandler = value -> {
        throw new UnhandledArgumentException(value);
    };

    @Override
    public <T extends Options> T addOptions(T instance) {
        definitions.add(instance);
        addFieldOptions(instance);
        addMethodOptions(instance);
        addAfterParseMethods(instance);
        return instance;
    }

    /**
     * Process all fields annotated with {@link Option}, and add their handlers.
     *
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
     * Process all methods annotated with {@link Option}, and add their handlers.
     *
     * @param options the options class being processed
     */
    private <T extends Options> void addMethodOptions(T options) {
        for (Method method : options.getClass().getDeclaredMethods()) {
            // ... with @Option annotation
            if (method.getDeclaredAnnotationsByType(Option.class).length > 0) {
                addHandler(new MethodOption(this, options, method), method);
            }
        }
    }

    /**
     * Process all methods annotated with {@link AfterParse}, and add them to the afterParseMethods list.
     *
     * @param options the options class being processed
     */
    private <T extends Options> void addAfterParseMethods(T options) {
        for (Method method : options.getClass().getDeclaredMethods()) {
            if (method.getDeclaredAnnotation(AfterParse.class) != null) {
                afterParseMethods.add(new AfterParseHandler(method, options));
            }
        }
    }

    /**
     * Add new option - a field or method.
     *
     * @param rawHandler a "plain" handler of field or method
     * @param member the field or method in question
     */
    private void addHandler(OptionHandler rawHandler, AccessibleObject member) {
        // Handle all the semantic quirks
        final OptionHandler handler = wrapHandler(rawHandler, member);

        // Register this option for mandatory checking
        mandatoryManager.add(handler, member);

        optionHandlerList.add(handler);

        // Add all @Option synonyms to the map
        Arrays.stream(member.getDeclaredAnnotationsByType(Option.class))
                .forEach(option -> mapOption(option.value(), handler));
    }

    /**
     * Add one @Option - concrete alias.
     *
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
     * Some options have different semantics. Because these generate too much combinations,
     * we handle them using decorators over the handlers.
     *
     * For now, we handle:
     *      - arrays, by aggregating the component type, yielding the final array at the end
     *      - primitive types, because they often require special handling
     *      - boolean options, because --verbose is a shorthand for "--verbose true"
     *      - range options, we need to check if the value is in the given range
     *
     * Please note that the order is important here, as it defines the final semantics.
     *
     * @param rawHandler a handler to be wrapped
     * @return handler, wrapped if applicable
     */
    public static OptionHandler wrapHandler(OptionHandler rawHandler, AccessibleObject member) {
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

    public DriverCache getDriverLocator() {
        return driverLocator;
    }

    public void setDriverLocator(DriverCache driverLocator) {
        this.driverLocator = driverLocator;
    }

    @Override
    public void parse(String[] args) throws UnhandledArgumentException {
        TokenList tokenList = new TokenList(args);

        while (tokenList.size() > 0) {
            final String optionToken = tokenList.remove();

            if (optionToken.equals("--")) {
                // The delimiter, rest are positional arguments
                while (tokenList.size() > 0) {
                    unexpectedParameterHandler.handle(tokenList.remove());
                }
                continue;
            }

            Optional<InternalOptionValue> maybeOptionValue = InternalOptionValueFactory.tryCreate(optionToken);

            if (!maybeOptionValue.isPresent()) {
                unexpectedParameterHandler.handle(optionToken);
                continue;
            }

            if (!optionHandlerMap.containsKey(maybeOptionValue.get().getName())) {
                // TODO: Consider (configurable?) warning on weird --positionalargument
                unexpectedParameterHandler.handle(optionToken);
                continue;
            }

            final InternalOptionValue optionValue = maybeOptionValue.get();
            final OptionHandler handler = optionHandlerMap.get(optionValue.getName());

            ValuePolicy valuePolicy = handler.getValuePolicy();

            if (tokenList.size() > 0)
                optionValue.completeValue(tokenList, valuePolicy);

            mandatoryManager.encountered(handler);
            final Class<?> type = handler.getType();

            if (valuePolicy == ValuePolicy.MANDATORY && !optionValue.hasValue()) {
                throw new MissingOptionValue(optionValue);
            }
            if (valuePolicy == ValuePolicy.NEVER && optionValue.hasValue()) {
                throw new InvalidOptionValue("Parameter " + optionValue.getName() + " cannot have an associated value.");
            }

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

        // Call all @AfterParse methods
        for (AfterParseHandler afterParse : afterParseMethods) {
            afterParse.invoke();
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
            Help annotation = options.getClass().getDeclaredAnnotation(Help.class);
            if (annotation != null) {
                builder.append(annotation.value()).append(endl);
            }

            for (OptionHandler handler : optionHandlerList) {
                if (handler.getDefinitionClass() != options)
                    continue;

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
