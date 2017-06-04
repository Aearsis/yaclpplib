package cz.cuni.mff.yaclpplib;

/**
 * Exception thrown when there are positional arguments present
 * and they were not requested by {@link ArgumentParser#requestPositionalArguments()}
 */
public class UnhandledArgumentException extends InvalidArgumentsException {
    /** Problematic option */
    private final String value;

    /**
     * Constructs a new exception for a specific option.
     * @param value unhandled option
     */
    public UnhandledArgumentException(String value) {
        super("Unknown argument found on the command line: " + value);
        this.value = value;
    }

    /**
     * Gets the option which caused this exception.
     * @return malicious option
     */
    public String getOptionValue() {
        return value;
    }
}
