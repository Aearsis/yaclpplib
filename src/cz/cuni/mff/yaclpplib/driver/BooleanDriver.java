package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.OptionValue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BooleanDriver implements Driver<Boolean> {

    private static Set<String> truishValues = new HashSet<>(Arrays.asList(
            "true", "1", "yes", "on"
    ));

    private static Set<String> falsyValues = new HashSet<>(Arrays.asList(
            "false", "0", "no", "off"
    ));

    @Override
    public Boolean parse(OptionValue x) throws InvalidOptionValue {
        if (truishValues.contains(x)) {
            return true;
        } else if (falsyValues.contains(x)) {
            return false;
        } else {
            throw new InvalidOptionValue("The value of " + x.getOption() + " can be either 'true' or 'false'.");
        }
    }

    @Override
    public Class getReturnType() {
        return String.class;
    }
}
