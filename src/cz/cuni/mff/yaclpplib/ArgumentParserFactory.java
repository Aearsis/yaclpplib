package cz.cuni.mff.yaclpplib;

import cz.cuni.mff.yaclpplib.driver.*;
import cz.cuni.mff.yaclpplib.implementation.ArgumentParserImpl;
import cz.cuni.mff.yaclpplib.implementation.DefaultHelpOption;

/**
 * A factory for creating ArgumentParser instances.
 */
public final class ArgumentParserFactory {

    /**
     * Creates an empty parser with no drivers attached.
     * The programmer is expected to create his own drivers and fill the parser himself.
     * @return an empty parser
     */
    public static ArgumentParser createPlainParser() {
        return new ArgumentParserImpl();
    }

    /**
     * Creates a parser filled with drivers for all primitive types.
     * @return an instance of parser
     */
    public static ArgumentParser createDefaultTypesParser() {
        final ArgumentParserImpl parser = (ArgumentParserImpl) createPlainParser();
        parser.addDriver(new VoidDriver());
        parser.addDriver(new StringDriver());
        parser.addDriver(new CharacterDriver());
        parser.addDriver(new BooleanDriver());
        return parser;
    }

    /**
     * Creates a parser filled with drivers for all primitive types.
     * The parser also includes a default handler for --help option,
     * which prints the help and exits the program.
     * @return an instance of parser
     */
    public static ArgumentParser create() throws InvalidSetupError {
        final ArgumentParser parser = createDefaultTypesParser();
        parser.addOptions(new DefaultHelpOption(parser));
        return parser;
    }
}
