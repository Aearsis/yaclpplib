package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.OptionValue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class VoidDriver implements Driver<Void> {

    @Override
    public Void parse(OptionValue x) throws InvalidOptionValue {
        if (x.hasValue()) {
            throw new InvalidOptionValue("There should not be a value of " + x.getOption() + ".");
        }

        return null;
    }

    @Override
    public Class<Void> getReturnType() {
        return Void.class;
    }
}
