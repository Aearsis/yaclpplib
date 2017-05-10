package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.OptionValue;

/**
 * Driver for enum subclasses. It is to be added dynamically for every enum subtype encountered in users' Options.
 *
 * This is a bit hacky because how hacky enums are in Java.
 */
@SuppressWarnings("unchecked")
public class GenericEnumDriver implements Driver {

    final private Class enumType;

    public GenericEnumDriver(Class enumType) {
        this.enumType = enumType;
    }

    @Override
    public Object parse(OptionValue x) throws InvalidOptionValue {
        try {
            return Enum.valueOf(enumType, x.getValue());
        } catch (IllegalArgumentException e) {
            throw new InvalidOptionValue("Invalid value for " + x.getOption());
        }
    }

    @Override
    public Class<?> getReturnType() {
        return enumType;
    }

}
