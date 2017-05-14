package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.*;
import cz.cuni.mff.yaclpplib.annotation.AfterParse;
import cz.cuni.mff.yaclpplib.annotation.Help;
import cz.cuni.mff.yaclpplib.annotation.Option;
import cz.cuni.mff.yaclpplib.driver.Driver;
import cz.cuni.mff.yaclpplib.driver.GenericEnumDriver;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class ArgumentParserImpl implements ArgumentParser {

    private final List<Options> definitions = new ArrayList<>();
    private final DriverStorage drivers = new CachedDriverStorage(new HashDriverStorage());

    private final Map<String, OptionBase> optionHandlerMap;
    private final List<OptionBase> optionHandlerList;
    private final List<AfterParseHandler> afterParseMethods;

    private int parseIndex;

    public ArgumentParserImpl() {
        optionHandlerMap = new HashMap<>();
        optionHandlerList = new ArrayList<>();
        afterParseMethods = new ArrayList<>();
    }

    private UnexpectedParameterHandler unexpectedParameterHandler = value -> {
        throw new UnhandledArgumentException(value);
    };

    private void mapOption(String text, OptionBase handler) {
        if (optionHandlerMap.containsKey(text)) {
            throw new InvalidSetupError("One option (" + text + ") can't be used at multiple methods or fields.");
        }
        optionHandlerMap.put(text, handler);
    }

    @SuppressWarnings("unchecked")
    private void addOptionHandler(OptionBase handler) {
        if (handler.getType().isEnum() && !drivers.contains(handler.getType())) {
            drivers.add(new GenericEnumDriver(handler.getType()));
        }
        optionHandlerList.add(handler);
    }

    @Override
    public <T extends Options> T addOptions(T instance) {
        // Add the instance the options instances list
        definitions.add(instance);

        // Process every field with @Option annotation
        for (Field field : instance.getClass().getDeclaredFields()) {
            if (field.getDeclaredAnnotationsByType(Option.class).length > 0) {
                final FieldOption fieldOption = new FieldOption(instance, field);
                addOptionHandler(fieldOption);
                Arrays.stream(field.getDeclaredAnnotationsByType(Option.class))
                        .forEach(option -> mapOption(option.value(), fieldOption));
            }
        }

        // Process every method
        for (Method method : instance.getClass().getDeclaredMethods()) {
            // ... with @Option annotation
            if (method.getDeclaredAnnotationsByType(Option.class).length > 0) {
                final MethodOption methodOption = new MethodOption(instance, method);
                addOptionHandler(methodOption);
                Arrays.stream(method.getDeclaredAnnotationsByType(Option.class))
                        .forEach(option -> mapOption(option.value(), methodOption));
            }
            // ... or with @AfterParse annotation
            if (method.getDeclaredAnnotation(AfterParse.class) != null) {
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                if (method.getParameterCount() != 0) {
                    throw new InvalidSetupError("An AfterParse method cannot have any parameters.");
                }
                if (method.getExceptionTypes().length > 0) {
                    for (Class exception : method.getExceptionTypes()) {
                        if (!(RuntimeException.class.isAssignableFrom(exception))) {
                            throw new InvalidSetupError("An AfterParse method must throw only runtime exceptions.");
                        }
                    }
                }
                afterParseMethods.add(new AfterParseHandler(method, instance));
            }
        }

        return instance;
    }

    public <T> Driver<T> addDriver(Driver<T> driver) throws DuplicateDriverError {
        drivers.add(driver);
        return driver;
    }

    private void parseLongOption(String option) {
        final String[] parts = option.split("=", 2);
        if (parts.length == 1) {
            // We have no value for it, is it even an option?
            if (optionHandlerMap.containsKey(parts[0])) {
                final OptionBase handler = optionHandlerMap.get(option);
                if (handler.getValuePolicy() == ValuePolicy.MANDATORY) {
                    // This combination is forbidden
                    throw new InvalidOptionValue("Parameter " + parts[0] + " must have an associated value.");
                }
                else {
                    final OptionValueBase optionValue = new OptionValueBase(this);
                    optionValue.setOption(parts[0]);
                    optionValue.setRawTokens(new String[] {parts[0]});
                    handler.haveTypedValue(optionValue, null);
                    return; // We are done
                }
            }
        }
        else {
            // We have a value, let's see if this option exists and a value is permitted
            if (optionHandlerMap.containsKey(parts[0])) {
                final OptionBase handler = optionHandlerMap.get(parts[0]);
                if (handler.getValuePolicy() == ValuePolicy.NEVER) {
                    throw new InvalidOptionValue("Parameter " + parts[0] + " cannot have an associated value.");
                }
                else {
                    final OptionValueBase optionValue = new OptionValueBase(this);
                    optionValue.setOption(parts[0]);
                    optionValue.setValue(parts[1]);
                    optionValue.setRawTokens(new String[] {option});
                    final Object typedValue = drivers.find(handler.getType()).parse(optionValue);
                    handler.haveTypedValue(optionValue, typedValue);
                    return; // Done
                }
            }
        }

        // Everything failed
        unexpectedParameterHandler.handle(option);
    }

    private void parseShortOption(String option, String nextOption) {
        if (optionHandlerMap.containsKey(option)) {
            final OptionBase handler = optionHandlerMap.get(option);
            final OptionValueBase optionValue = new OptionValueBase(this);
            optionValue.setOption(option);

            switch (handler.getValuePolicy()) {
                case NEVER:
                    optionValue.setRawTokens(new String[] {option});
                    handler.haveTypedValue(optionValue, null);
                    break;
                case MANDATORY:
                    if (nextOption == null) {
                        throw new InvalidOptionValue(
                                "No option value was specified for option " + optionValue.getOption());
                    }
                    optionValue.setValue(nextOption);
                    optionValue.setRawTokens(new String[] {option, nextOption});
                    // We consumed the value so we need to advance
                    ++parseIndex;
                    final Object typedValue1 = drivers.find(handler.getType()).parse(optionValue);
                    handler.haveTypedValue(optionValue, typedValue1);
                    break;
                case OPTIONAL:
                    if (nextOption == null || nextOption.startsWith("-")) {
                        optionValue.setRawTokens(new String[] {option});
                        handler.haveTypedValue(optionValue, null);
                    }
                    else {
                        optionValue.setValue(nextOption);
                        optionValue.setRawTokens(new String[] {option, nextOption});
                        // We consumed the value so we need to advance
                        ++parseIndex;
                        final Object typedValue2 = drivers.find(handler.getType()).parse(optionValue);
                        handler.haveTypedValue(optionValue, typedValue2);
                    }
                    break;
            }
        }
        else {
            unexpectedParameterHandler.handle(option);
        }
    }

    @Override
    public void parse(String[] args) throws UnhandledArgumentException {
        // We use parse index because sometimes, we have to advance by 2 (-p value)
        parseIndex = 0;
        // Process every argument
        while (parseIndex < args.length) {
            final String option = args[parseIndex];
            final String nextOption = (parseIndex != args.length - 1) ? args[parseIndex + 1] : null;

            if (option.equals("--")) {
                // The delimiter, rest are positional arguments
                ++parseIndex;
                while (parseIndex != args.length) {
                    unexpectedParameterHandler.handle(args[parseIndex]);
                }
            }
            else if (option.matches("--[a-zA-Z0-9].*")) {
                parseLongOption(option);
            }
            else if (option.matches("-[a-zA-Z0-9]")) {
                parseShortOption(option, nextOption);
            }
            else {
                unexpectedParameterHandler.handle(option);
            }
            ++parseIndex;
        }

        // Finish all the option arrays we were filling
        for (OptionBase optionBase : optionHandlerList) {
            optionBase.finish();
        }

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

        for (Options options : definitions) {
            Help annotation = options.getClass().getDeclaredAnnotation(Help.class);
            if (annotation != null) {
                builder.append("\n").append(annotation.value()).append("\n");
            }

            // TODO: We need a list of options for every option definition
            // for all @Option:
                // print help line
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
