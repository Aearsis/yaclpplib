package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.OptionValue;

public class ShortDriver implements Driver<Short> {

    @Override
    public Short parse(OptionValue x) throws InvalidOptionValue {
        try {
            return Short.parseShort(x.getValue());
        } catch (NumberFormatException e) {
            throw new InvalidOptionValue("Option " + x.getName() + " requires an integer value between -32768 and 32767.");
        }
    }

    @Override
    public Class<Short> getReturnType() {
        return Short.class;
    }
}
