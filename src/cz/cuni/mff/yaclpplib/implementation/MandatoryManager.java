package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.MissingMandatoryOptionException;
import cz.cuni.mff.yaclpplib.annotation.Mandatory;
import cz.cuni.mff.yaclpplib.annotation.Option;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Takes care of mandatory options and keeps tracking which ones were encountered.
 */
public class MandatoryManager {

    final private Map<OptionHandler, Boolean> encounteredOptions;

    public MandatoryManager() {
        encounteredOptions = new HashMap<>();
    }

    /**
     * Called when a new option is being registered. Starts tracking the given option.
     * @param optionHandler handler representing the option
     */
    public void add(OptionHandler optionHandler) {
        if (optionHandler.getHandledObject().getDeclaredAnnotation(Mandatory.class) != null) {
            encounteredOptions.put(optionHandler, false);
        }
    }

    /**
     * Call when an option is encountered.
     * @param optionHandler the handler of the encountered option
     */
    public void encountered(OptionHandler optionHandler) {
        if (encounteredOptions.containsKey(optionHandler)) {
            encounteredOptions.put(optionHandler, true);
        }
    }

    /**
     * Checks whether all mandatory options were present in the string given to the parser.
     * @throws MissingMandatoryOptionException when at least one option wasn't present
     */
    public void check() throws MissingMandatoryOptionException {
        final List<String> missingOptions = new ArrayList<>();
        for (Map.Entry<OptionHandler, Boolean> entry : encounteredOptions.entrySet()) {
            if (!entry.getValue()) {
                final Option[] options = entry.getKey().getHandledObject().getAnnotationsByType(Option.class);
                missingOptions.add(options[0].value());
            }
        }
        if (missingOptions.size() > 0) {
            throw new MissingMandatoryOptionException(missingOptions.toArray(new String[missingOptions.size()]));
        }
    }
}
