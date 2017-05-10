package cz.cuni.mff.yaclpplib;

/**
 * Signalizes, that the setup of ArgumentParser is invalid.
 */
public class InvalidSetupError extends Error {

    public InvalidSetupError() {
    }

    public InvalidSetupError(String message) {
        super(message);
    }
}
