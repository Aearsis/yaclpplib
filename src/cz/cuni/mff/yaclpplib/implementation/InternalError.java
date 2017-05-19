package cz.cuni.mff.yaclpplib.implementation;

/**
 * A serious error in the library itself.
 */
public class InternalError extends Error {
    public InternalError() {
    }

    public InternalError(String message) {
        super(message);
    }
}
