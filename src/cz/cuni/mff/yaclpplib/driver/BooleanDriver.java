package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.OptionValue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BooleanDriver implements Driver<Boolean> {

    private static Set<String> truthyValues = new HashSet<>(Arrays.asList(
            "true", "t", "1", "yes", "y", "on", "enable", "enabled", null
    ));

    private static Set<String> falsyValues = new HashSet<>(Arrays.asList(
            "false", "f", "0", "no", "n", "off", "disable", "disabled"
    ));

    @Override
    public Boolean parse(OptionValue x) throws InvalidOptionValue {
        final String value = x.getValue();
        if (truthyValues.contains(value)) {
            return true;
        } else if (falsyValues.contains(value)) {
            return false;
        } else {
            throw new InvalidOptionValue("The value of " + x.getOption() + " can be either 'true' or 'false'.");
        }
    }

    @Override
    public Class<Boolean> getReturnType() {
        return Boolean.class;
    }
}
