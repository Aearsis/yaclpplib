package cz.cuni.mff.yaclpplib;

import cz.cuni.mff.yaclpplib.annotation.Help;

import java.util.List;

/**
 * An interface for classes responsible for parsing command line arguments.
 */
public interface ArgumentParser {

    /**
     * Register instance of Options class. It will be filled with argument values, when you call parse.
     * @param instance An instance of Options interface
     * @param <T> Type of your Options class
     * @return your instance (so you can use it as a oneliner)
     */
    <T extends Options> T addOptions(T instance);

    /**
     * Read the arguments, fill all argument classes
     * @param args array of arguments given to main method
     * @throws UnhandledArgumentException When you don't request positional arguments and they are present
     */
    void parse(String[] args) throws UnhandledArgumentException;

    /**
     * Get a nicely formatted, structured help about available arguments.
     *
     * Uses the {@link Help} annotation to document arguments and modules.
     * @return
     */
    String getHelp();

    /**
     * Prints structured help in standard error output. The help is constructed
     * using {@link Help} annotation.
     */
    default void printHelp() {
        System.err.println(getHelp());
    }

    /**
     * Creates a list, which will be filled with positional arguments when the argument list is parsed.
     * Do not use in conjunction with {@link ArgumentParser::setUnexpectedParameterHandler},
     * because then all parameters are expected.
     *
     * @return reference to a list which will later be filled with positional arguments
     */
    List<String> requestPositionalArguments();

    /**
     * Changes the way parser handles unknown arguments.
     * Do not use in conjunction with {@link ArgumentParser::requestPositionalArguments},
     * because then all parameters are expected.
     *
     * @param handler new handler that would be called on unexpected argument
     */
    void setUnexpectedParameterHandler(UnexpectedParameterHandler handler);

    /**
     * Creates a list, which will be filled with plain arguments when the argument list is parsed.
     * @return reference to a list which will later be filled with plain arguments
     */
    default List<String> requestPlainArguments() {
        return requestPositionalArguments();
    }
}
