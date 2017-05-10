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
        // TODO: create {Method,Field}Options, register them
        return instance;
    }

    public <T> Driver<T> addDriver(Driver<T> driver) throws DuplicateDriverError {
        drivers.add(driver);
        return driver;
    }

    @Override
    public void parse(String[] args) throws UnhandledArgumentException {
        // TODO:
        // identify [option] [value]:
            // unexpected parameter XOR
            // find OptionBase
            // let it sort out

        // for every option:
            // call finish

        // for every AfterParse:
            // call it

        for (String s : args) {
            unexpectedParameterHandler.handle(s);
        }

        // TODO: call finish on all TheOptions
    }

    @Override
    public void setUnexpectedParameterHandler(UnexpectedParameterHandler handler) {
        unexpectedParameterHandler = handler;
    }

    @Override
    public String getHelp() {
        // For all options:
            // for all @Option:
                // print help line
        return "";
    }

    @Override
    public List<String> requestPositionalArguments() {
        final ArrayList<String> positionalArgumentsList = new ArrayList<>();
        setUnexpectedParameterHandler(positionalArgumentsList::add);
        return positionalArgumentsList;
    }

}
