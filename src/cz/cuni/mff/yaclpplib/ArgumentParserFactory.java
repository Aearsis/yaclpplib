package cz.cuni.mff.yaclpplib;

import cz.cuni.mff.yaclpplib.driver.*;
import cz.cuni.mff.yaclpplib.implementation.ArgumentParserImpl;
import cz.cuni.mff.yaclpplib.implementation.DefaultHelpOption;

public final class ArgumentParserFactory {

    public static ArgumentParser createPlainParser() {
        return new ArgumentParserImpl();
    }

    public static ArgumentParser createDefaultTypesParser() throws InvalidSetupError {
        final ArgumentParserImpl parser = (ArgumentParserImpl) createPlainParser();
        parser.addDriver(new VoidDriver());
        parser.addDriver(new StringDriver());
        parser.addDriver(new IntegerDriver());
        parser.addDriver(new BooleanDriver());
        return parser;
    }

    public static ArgumentParser create() throws InvalidSetupError {
        final ArgumentParser parser = createDefaultTypesParser();
        parser.addOptions(new DefaultHelpOption());
        return parser;
    }
}
