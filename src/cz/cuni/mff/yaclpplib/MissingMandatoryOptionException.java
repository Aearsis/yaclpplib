package cz.cuni.mff.yaclpplib;

/**
 * Thrown by the library whenever a @Mandatory option is not present on the command line.
 */
public class MissingMandatoryOptionException extends RuntimeException {

    final private String[] optionNames;

    public MissingMandatoryOptionException(String[] optionNames) {
        assert optionNames.length > 0;
        this.optionNames = optionNames;
    }

    public String[] getOptionNames() {
        return optionNames;
    }

    /**
     * Provided for convenience, returns any name of the missing option (as provided in the @Option annotation).
     *
     * @return any name the missing option have
     */
    public String getOptionName() {
        return optionNames[0];
    }

}
