package cz.cuni.mff.yaclpplib;

import cz.cuni.mff.yaclpplib.implementation.OptionHandler;

import java.util.Arrays;

/**
 * Thrown by the library whenever a @Mandatory option is not present on the command line.
 */
public class MissingMandatoryOptionException extends RuntimeException {

    final private OptionHandler[] options;

    public MissingMandatoryOptionException(OptionHandler[] options) {
        assert options.length > 0;
        this.options = options;
    }

    public String[] getOptionNames(){
        return Arrays.stream(options).map(OptionHandler::getAnyOptionName).toArray(String[]::new);
    }

    /**
     * Provided for convenience, returns any name of the missing option (as provided in the @Option annotation).
     *
     * @return any name the missing option have
     */
    public String getOptionName() {
        return getOptionNames()[0];
    }

}
