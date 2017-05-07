package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.*;
import cz.cuni.mff.yaclpplib.driver.Driver;

import java.util.ArrayList;
import java.util.List;

public class ArgumentParserImpl implements ArgumentParser {

    private final List<Options> definitions = new ArrayList<>();
    private final DriverStorage drivers = new CachedDriverStorage(new HashDriverStorage());

    private UnexpectedParameterHandler unexpectedParameterHandler = value -> {
        throw new UnhandledArgumentException(value);
    };

    @Override
    public <T extends Options> T addOptions(T instance) {
        definitions.add(instance);
        return instance;
    }

    public <T> Driver<T> addDriver(Driver<T> driver) throws DuplicateDriverError {
        drivers.add(driver);
        return driver;
    }

    @Override
    public void parse(String[] args) throws UnhandledArgumentException {
        for (String s : args) {
            unexpectedParameterHandler.handle(s);
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
