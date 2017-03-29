package cz.cuni.mff.yaclpplib;

import cz.cuni.mff.yaclpplib.annotation.Help;

import java.util.List;

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
     * @param args
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

    default void printHelp() {
        System.err.println(getHelp());
    }

    List<String> requestPositionalArguments();

    default List<String> requestPlainArguments() {
        return requestPositionalArguments();
    }
}
