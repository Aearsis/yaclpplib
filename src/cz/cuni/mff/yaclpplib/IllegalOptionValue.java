package cz.cuni.mff.yaclpplib;

import cz.cuni.mff.yaclpplib.annotation.Range;
import cz.cuni.mff.yaclpplib.annotation.Option;

/**
 * Thrown when user constraint on an option failed. It is thrown by the {@link Range} validator
 * and you may throw it from your {@link Option} annotated methods.
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
