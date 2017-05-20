package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.OptionValue;

public class DoubleDriver implements Driver<Double> {

    @Override
    public Double parse(OptionValue x) throws InvalidOptionValue {
        try {
            return Double.parseDouble(x.getValue());
        } catch (NumberFormatException e) {
            throw new InvalidOptionValue("Option " + x.getOption() + " requires a real value.");
        }
    }

    @Override
    public Class<Double> getReturnType() {
        return Double.class;
    }
}
