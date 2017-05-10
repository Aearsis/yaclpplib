package cz.cuni.mff.yaclpplib;

/**
 * Thrown when user constraint on an option failed. It is thrown by the @Range validator
 * and you may throw it from your @Option annotated methods.
 */
public class IllegalOptionValue extends RuntimeException {

    public IllegalOptionValue() {
    }

    public IllegalOptionValue(String message) {
        super(message);
    }

    public IllegalOptionValue(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalOptionValue(Throwable cause) {
        super(cause);
    }

    public IllegalOptionValue(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
