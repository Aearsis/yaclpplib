package cz.cuni.mff.yaclpplib;

import cz.cuni.mff.yaclpplib.implementation.OptionHandler;
import cz.cuni.mff.yaclpplib.annotation.Mandatory;
import cz.cuni.mff.yaclpplib.annotation.Option;

import java.util.Arrays;

/**
 * Thrown by the library whenever a {@link Mandatory} option is not present on the command line.
 */
public class MissingMandatoryOptionException extends RuntimeException {

    final private OptionHandler[] options;

    public MissingMandatoryOptionException(OptionHandler[] options) {
        assert options.length > 0;
        this.options = options;
    }

    /**
     * Provides a list of missing options.
     * @return an array with all missing options
     */
    public String[] getOptionNames(){
        return Arrays.stream(options).map(OptionHandler::getAnyOptionName).toArray(String[]::new);
    }

    /**
     * Provided for convenience, returns any name of the missing option (as provided in the {@link Option} annotation).
     *
     * @return any name the missing option have
     */
    public String getOptionName() {
        return getOptionNames()[0];
    }

    @Override
    public String getMessage() {
        return String.format("Missing mandatory option%s: %s",
                options.length > 1 ? "s" : "",
                String.join(",", getOptionNames()));
    }
}
