package cz.cuni.mff.yaclpplib;

/**
 * Container carrying additional information about passed option.
 */
public interface OptionValue {

    /**
     * @return -s, --long
     */
    String getName();

    /**
     * Should return null iff hasValue() returns false.
     *
     * @return "value", null
     */
    String getValue();

    boolean hasValue();

    /**
     * @return {"--long=value"}, {"--long", "value"}, {"-s", "value"}, {"-svalue"}
     */
    String[] getRawTokens();
}
