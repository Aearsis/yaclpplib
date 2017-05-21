package cz.cuni.mff.yaclpplib;

/**
 * Exception thrown when the value cannot be parsed into desired type.
 */
public class InvalidOptionValue extends RuntimeException {

    InvalidOptionValue() {
    }

    public InvalidOptionValue(String message) {
        super(message);
    }

    public InvalidOptionValue(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidOptionValue(Throwable cause) {
        super(cause);
    }

    public InvalidOptionValue(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
