package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.OptionValue;

public class VoidDriver implements Driver<Void> {

    @Override
    public Void parse(OptionValue x) throws InvalidOptionValue {
        if (x.hasValue()) {
            throw new InvalidOptionValue("There should not be a value of " + x.getName() + ".");
        }

        return null;
    }

    @Override
    public Class<Void> getReturnType() {
        return Void.class;
    }
}
