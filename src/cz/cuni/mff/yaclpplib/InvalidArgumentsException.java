package cz.cuni.mff.yaclpplib;

/**
 * A common base for all exceptions regarding invalid input on the command line.
 */
public class InvalidArgumentsException extends Exception {

    public InvalidArgumentsException() {
        super("Validation failed.");
    }

    public InvalidArgumentsException(String message) {
        super(message);
    }

    public InvalidArgumentsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidArgumentsException(Throwable cause) {
        super(cause);
    }

    public InvalidArgumentsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof InvalidArgumentsException
                && getMessage().equals(((InvalidArgumentsException) other).getMessage());
    }

    @Override
    public int hashCode() {
        return getMessage().hashCode();
    }
}

