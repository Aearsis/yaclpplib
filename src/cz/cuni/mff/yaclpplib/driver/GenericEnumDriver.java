package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.OptionValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Driver for enum subclasses. It is to be created dynamically for every
 * enum subtype encountered in users' Options.
 *
 * This is a bit hacky because how hacky enums are in Java.
 */
@SuppressWarnings("unchecked")
public class GenericEnumDriver implements Driver {

    final private Class enumType;
    final private Map<String, Object> enumValues;

    public GenericEnumDriver(Class enumType) {
        this.enumType = enumType;
        this.enumValues = new HashMap<>();
        for (Object constant : enumType.getEnumConstants()) {
            enumValues.put(constant.toString().toLowerCase(), constant);
        }
    }

    @Override
    public Object parse(OptionValue x) throws InvalidOptionValue {
        if (!x.hasValue())
            return null;
        String value = x.getValue().toLowerCase();
        if (enumValues.containsKey(value)) {
            return enumValues.get(value);
        }
        else {
            throw new InvalidOptionValue("Invalid value for " + x.getName());
        }
    }

    @Override
    public Class<?> getReturnType() {
        return enumType;
    }

}
