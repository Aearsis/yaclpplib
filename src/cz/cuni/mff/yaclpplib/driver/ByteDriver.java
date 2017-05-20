package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.OptionValue;

public class ByteDriver implements Driver<Byte> {

    @Override
    public Byte parse(OptionValue x) throws InvalidOptionValue {
        try {
            return Byte.parseByte(x.getValue());
        } catch (NumberFormatException e) {
            throw new InvalidOptionValue("Option " + x.getName() + " requires an integer value between -128 and 127.");
        }
    }

    @Override
    public Class<Byte> getReturnType() {
        return Byte.class;
    }
}
