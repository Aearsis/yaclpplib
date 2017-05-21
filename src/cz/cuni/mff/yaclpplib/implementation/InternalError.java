package cz.cuni.mff.yaclpplib.implementation;

/**
 * A serious error in the library itself.
 * If you see this error in production build,
 * please report it to the authors. This should never be seen.
 */
public class InternalError extends Error {
    public InternalError() {
    }

    public InternalError(String message) {
        super(message);
    }
}
