package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.*;
import cz.cuni.mff.yaclpplib.annotation.*;
import cz.cuni.mff.yaclpplib.driver.*;

import java.lang.reflect.*;
import java.util.*;

public class ArgumentParserImpl implements ArgumentParser {

    private final List<Options> definitions = new ArrayList<>();
    private final DriverCache drivers = new DriverCache<>(new HashDriverLocator());

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

    private void addHandler(OptionHandler rawHandler, AccessibleObject member) {
        final OptionHandler handler = OptionHandler.wrap(rawHandler);
        optionHandlerList.add(handler);
        Arrays.stream(member.getDeclaredAnnotationsByType(Option.class))
                .forEach(option -> mapOption(option.value(), handler));
    }

    @Override
    public <T extends Options> T addOptions(T instance) {
        // Add the instance the options instances list
        definitions.add(instance);

        // Process every field with @Option annotation
        for (Field field : instance.getClass().getDeclaredFields()) {
            if (field.getDeclaredAnnotationsByType(Option.class).length > 0) {
                addHandler(new FieldOption(instance, field), field);
            }
        }

        // Process every method
        for (Method method : instance.getClass().getDeclaredMethods()) {
            // ... with @Option annotation
            if (method.getDeclaredAnnotationsByType(Option.class).length > 0) {
                addHandler(new MethodOption(instance, method), method);
            }
            // ... or with @AfterParse annotation
            if (method.getDeclaredAnnotation(AfterParse.class) != null) {
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

                final Class<?> type = handler.getType();
                handler.optionFound(matchedOptionValue, drivers.getDriverFor(type));
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
