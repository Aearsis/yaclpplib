package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.OptionValue;

public class IntegerDriver implements Driver<Integer> {

    @Override
    public Integer parse(OptionValue x) throws InvalidOptionValue {
        try {
            return Integer.parseInt(x.getValue());
        } catch (NumberFormatException e) {
            throw new InvalidOptionValue(e.getMessage());
        }
    }

    @Override
    public Class<Integer> getReturnType(){
        return Integer.class;
    }
}
