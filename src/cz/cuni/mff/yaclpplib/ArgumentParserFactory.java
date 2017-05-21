package cz.cuni.mff.yaclpplib;

import cz.cuni.mff.yaclpplib.driver.*;
import cz.cuni.mff.yaclpplib.implementation.ArgumentParserImpl;
import cz.cuni.mff.yaclpplib.implementation.DefaultHelpOption;
import cz.cuni.mff.yaclpplib.implementation.drivers.DriverCache;
import cz.cuni.mff.yaclpplib.implementation.drivers.HashDriverLocator;

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

        final HashDriverLocator driverLocator = new HashDriverLocator();
        driverLocator.add(new VoidDriver());
        driverLocator.add(new StringDriver());
        driverLocator.add(new CharacterDriver());
        driverLocator.add(new BooleanDriver());

        final DriverCache driverCache = new DriverCache();
        driverCache.addDriverLocator(driverLocator);
        driverCache.addDriverLocator(new EnumDriverFactory());
        driverCache.addDriverLocator(new StringConstructableDriverFactory());

        parser.setDriverLocator(driverCache);
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
