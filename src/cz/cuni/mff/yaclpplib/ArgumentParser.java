package cz.cuni.mff.yaclpplib;

import cz.cuni.mff.yaclpplib.annotation.Help;

import java.util.List;

/**
 * An interface for classes responsible for parsing command line arguments.
 */
public interface ArgumentParser {

    /**
     * Register instance of Options class. It will be filled with argument values, when you call parse.
     *
     * @param instance An instance of OptionList interface
     * @param <T> Type of your OptionList class
     * @return your instance (so you can use it as a oneliner)
     */
    <T extends Options> T addOptions(T instance) throws InvalidSetupError;

    /**
     * Read the arguments, fill all argument classes. <br/>
     *
     * It is generally possible to call this method multiple times, but with certain limitations:
     * <ul>
     *     <li>The set of arguments given in the first call must be valid on its own.</li>
     *     <li>Method options taking arrays will be called multiple times, with partial arrays.</li>
     * </ul>
     *
     * @param args array of arguments given to main method
     * @throws UnhandledArgumentException When you don't request positional arguments and they are present
     * @throws InvalidOptionValue When a value cannot be parsed into the type of option
     * @throws MissingOptionValue When an option is unexpectedly missing a value
     * @throws IllegalOptionValue When a value is rejected by a method option or @Range validator
     * @throws MissingMandatoryOptionException When a @Mandatory option is missing
     * @throws RuntimeException from your own {@link cz.cuni.mff.yaclpplib.annotation.AfterParse} methods
     */
    void parse(String[] args) throws UnhandledArgumentException;

    /**
     * Get a nicely formatted, structured help about available arguments.
     *
     * Uses the {@link Help} annotation to document arguments and modules.
     * @return Structured help ready to print on output
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
     * Changes the way parser handles unknown arguments. <br/>
     * Do not use in conjunction with {@link ArgumentParser#requestPositionalArguments},
     * because then all parameters are expected.
     *
     * @param handler new handler that would be called on unexpected argument
     */
    void setUnexpectedParameterHandler(UnexpectedParameterHandler handler);

    /**
     * Creates a list, which will be filled with positional arguments when the argument list is parsed. <br/>
     * Do not use in conjunction with {@link ArgumentParser#setUnexpectedParameterHandler},
     * because then all parameters are expected.
     *
     * @return reference to a list which will later be filled with positional arguments
     */
    List<String> requestPositionalArguments();

    /**
     * Creates a list, which will be filled with plain arguments when the argument list is parsed.
     * @return reference to a list which will later be filled with plain arguments
     */
    default List<String> requestPlainArguments() {
        return requestPositionalArguments();
    }
}
