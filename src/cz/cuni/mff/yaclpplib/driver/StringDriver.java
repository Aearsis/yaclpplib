package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.OptionValue;
import cz.cuni.mff.yaclpplib.InvalidOptionValue;

public class StringDriver implements Driver<String> {

    @Override
    public String parse(OptionValue x) throws InvalidOptionValue {
        return x.getValue();
    }

    @Override
    public Class getReturnType() {
        return String.class;
    }
}
