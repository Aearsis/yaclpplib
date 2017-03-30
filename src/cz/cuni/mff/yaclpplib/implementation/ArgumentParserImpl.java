package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.Options;
import cz.cuni.mff.yaclpplib.UnexpectedParameterHandler;
import cz.cuni.mff.yaclpplib.UnhandledArgumentException;
import cz.cuni.mff.yaclpplib.driver.Driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArgumentParserImpl implements ArgumentParser {

    private final List<Options> definitions = new ArrayList<>();
    private final Map<Class, Driver> drivers = new HashMap<>();

    private UnexpectedParameterHandler unexpectedParameterHandler = value -> {
        throw new UnhandledArgumentException(value);
    };

    @Override
    public <T extends Options> T addOptions(T instance) {
        definitions.add(instance);
        return instance;
    }

    public void addDriver(Driver driver) {
        drivers.put(driver.getReturnType(), driver);
    }

    @Override
    public void parse(String[] args) throws UnhandledArgumentException {
        for (String s : args) {
            // unexpectedParameterHandler.handle(s);
        }
    }

    @Override
    public void setUnexpectedParameterHandler(UnexpectedParameterHandler handler) {
        unexpectedParameterHandler = handler;
    }

    @Override
    public String getHelp() {
        return "";
    }

    @Override
    public List<String> requestPositionalArguments() {
        final ArrayList<String> positionalArgumentsList = new ArrayList<>();
        setUnexpectedParameterHandler(positionalArgumentsList::add);
        return positionalArgumentsList;
    }
}
