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

    private final Map<String, OptionHandler> optionHandlerMap;
    private final List<OptionHandler> optionHandlerList;
    private final List<AfterParseHandler> afterParseMethods;

    public ArgumentParserImpl() {
        optionHandlerMap = new HashMap<>();
        optionHandlerList = new ArrayList<>();
        afterParseMethods = new ArrayList<>();
    }

    private UnexpectedParameterHandler unexpectedParameterHandler = value -> {
        throw new UnhandledArgumentException(value);
    };

    private void mapOption(String text, OptionHandler handler) {
        if (optionHandlerMap.containsKey(text)) {
            throw new InvalidSetupError("One option (" + text + ") can't be used at multiple methods or fields.");
        }
        optionHandlerMap.put(text, handler);
    }

    @SuppressWarnings("unchecked")
    private void addOptionHandler(OptionHandler handler) {
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
            }

            InternalOptionValue matchedOptionValue = null;

            if (LongOptionValue.matches(optionToken)) {
                matchedOptionValue = new LongOptionValue(this, optionToken);
            }
            else if (ShortOptionValue.matches(optionToken)) {
                matchedOptionValue = new ShortOptionValue(this, optionToken);
            }

            if (matchedOptionValue != null && optionHandlerMap.containsKey(matchedOptionValue.getOption())) {
                final OptionHandler handler = optionHandlerMap.get(matchedOptionValue.getOption());

                if (tokenList.size() > 0)
                    matchedOptionValue.completeValue(tokenList, handler.getValuePolicy());

                handler.optionFound(matchedOptionValue, drivers.find(handler.getType()));
            }
            else {
                unexpectedParameterHandler.handle(optionToken);
            }
        }

        // Finish all the option arrays we were filling
        for (OptionHandler handler : optionHandlerList) {
            handler.finish();
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
