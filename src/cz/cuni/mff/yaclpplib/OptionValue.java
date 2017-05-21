package cz.cuni.mff.yaclpplib;

/**
 * Container carrying additional information about passed option.
 */
public interface OptionValue {

    /**
     * The name of the option, such as "-s" or "--long".
     *
     * @return -s, --long
     */
    String getName();

    /**
     * Should return null if hasValue() returns false.
     *
     * @return "value", null
     */
    String getValue();

    /**
     * Checks if the instance has a set value.
     * 
     * @return true if the instance has a value
     */
    boolean hasValue();

    /**
     * Gives an array of raw tokens, as found before parsing them.
     *
     * @return {"--long=value"}, {"--long", "value"}, {"-s", "value"}, {"-svalue"}
     */
    String[] getRawTokens();
}
