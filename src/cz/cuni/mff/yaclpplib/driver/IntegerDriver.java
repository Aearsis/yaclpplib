package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.OptionValue;

public class IntegerDriver implements Driver<Integer> {

    @Override
    public Integer parse(OptionValue x) throws InvalidOptionValue {
        return Integer.parseInt(x.getValue());
    }

    @Override
    public Class getReturnType() {
        return String.class;
    }
}
