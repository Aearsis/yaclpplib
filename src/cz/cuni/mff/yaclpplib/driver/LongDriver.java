package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.OptionValue;

public class LongDriver implements Driver<Long> {

    @Override
    public Long parse(OptionValue x) throws InvalidOptionValue {
        try {
            return Long.parseLong(x.getValue());
        } catch (NumberFormatException e) {
            throw new InvalidOptionValue("Option " + x.getOption() + " requires an integer value.");
        }
    }

    @Override
    public Class<Long> getReturnType() {
        return Long.class;
    }
}
