package cz.cuni.mff.yaclpplib;

/**
 * <p>Defines an interface for handlers called when an unexpected parameter appears.</p>
 * <p>Allows overriding the default behaviour of parser when such parameter is encountered.</p>
 */
public interface UnexpectedParameterHandler {
    /**
     * A method called when an unexpected option is encountered.
     * @param value contains the encountered option
     * @throws UnhandledArgumentException when the handle can't process the option by itself
     */
    void handle(String value) throws UnhandledArgumentException;
}
