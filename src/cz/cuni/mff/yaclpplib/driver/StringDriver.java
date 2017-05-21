package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.OptionValue;
import cz.cuni.mff.yaclpplib.InvalidOptionValue;

/**
 * We need this to allow null strings.
 */
public class StringDriver implements Driver<String> {

    @Override
    public String parse(OptionValue x) throws InvalidOptionValue {
        if (x.getValue() == null) {
            throw new InvalidOptionValue("String option must have a value.");
        }

        return x.getValue();
    }

    @Override
    public Class<String> getReturnType() {
        return String.class;
    }
}
