package cz.cuni.mff.yaclpplib;

/**
 * Container carrying additional information about passed option.
 */
public interface OptionValue {

    /**
     * @return the parser which parsed this option and created this instance
     */
    ArgumentParser getParser();

    /**
     * @return -s, --long
     */
    String getOption();

    /**
     * @return "value", null
     */
    String getValue();

    /**
     * @return {"--long=value"}, {"--long", "value"}, {"-s", "value"}, {"-svalue"}
     */
    String[] getRawTokens();
}
