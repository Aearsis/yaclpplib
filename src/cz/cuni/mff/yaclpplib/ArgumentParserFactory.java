package cz.cuni.mff.yaclpplib;


import cz.cuni.mff.yaclpplib.driver.StringDriver;
import cz.cuni.mff.yaclpplib.implementation.ArgumentParserImpl;
import cz.cuni.mff.yaclpplib.implementation.DefaultHelpOption;

public final class ArgumentParserFactory {

    public static ArgumentParser createPlainParser() {
        return new ArgumentParserImpl();
    }

    public static ArgumentParser createDefaultTypesParser() {
        final ArgumentParserImpl parser = (ArgumentParserImpl) createPlainParser();
        parser.addDriver(new StringDriver());
        return parser;
    }

    public static ArgumentParser create() {
        final ArgumentParser parser = createDefaultTypesParser();
        parser.addOptions(new DefaultHelpOption());
        return parser;
    }
}
