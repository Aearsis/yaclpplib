package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.OptionValue;

public class FloatDriver implements Driver<Float> {

    @Override
    public Float parse(OptionValue x) throws InvalidOptionValue {
        try {
            return Float.parseFloat(x.getValue());
        } catch (NumberFormatException e) {
            throw new InvalidOptionValue("Option " + x.getOption() + " requires a real value.");
        }
    }

    @Override
    public Class<Float> getReturnType() {
        return Float.class;
    }
}
