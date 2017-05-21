package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.MissingMandatoryOptionException;
import cz.cuni.mff.yaclpplib.annotation.Mandatory;

import java.lang.reflect.AccessibleObject;
import java.util.*;

/**
 * Takes care of mandatory options and keeps tracking which ones were encountered.
 */
public class MandatoryManager {

    private final Set<OptionHandler> missingOptions = new HashSet<>();

    /**
     * Called when a new option is being registered. Starts tracking the given option.
     * @param optionHandler handler representing the option
     */
    public void add(OptionHandler optionHandler, AccessibleObject member) {
        if (member.getDeclaredAnnotation(Mandatory.class) != null) {
            missingOptions.add(optionHandler);
        }
    }

    /**
     * Call when an option is encountered.
     * @param optionHandler the handler of the encountered option
     */
    public void encountered(OptionHandler optionHandler) {
        missingOptions.remove(optionHandler);
    }

    /**
     * Checks whether all mandatory options were present in the string given to the parser.
     * @throws MissingMandatoryOptionException when at least one option wasn't present
     */
    public void check() throws MissingMandatoryOptionException {
        if (missingOptions.size() > 0) {
            throw new MissingMandatoryOptionException(missingOptions.toArray(new OptionHandler[missingOptions.size()]));
        }
    }
}
