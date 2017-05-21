package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.OptionValue;
import cz.cuni.mff.yaclpplib.annotation.CaseSensitive;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Driver for enum subclasses, that are annotated by {@link CaseSensitive} annotation.
 * It is to be created dynamically for every enum subtype encountered in users' Options. <br/>
 */
@SuppressWarnings("unchecked")
public class GenericEnumDriver implements Driver {

    final private Class enumType;
    final private Map<String, Object> enumValues = new HashMap<>();

    public GenericEnumDriver(Class enumType) {
        this.enumType = enumType;
        Arrays.stream(enumType.getEnumConstants()).forEach((c) -> enumValues.put(c.toString(), c));
    }

    @Override
    public Object parse(OptionValue optionValue) throws InvalidOptionValue {
        if (!enumValues.containsKey(optionValue.getValue())) {
            throw new InvalidOptionValue("Invalid value for " + optionValue.getName());
        }

        return enumValues.get(optionValue.getValue());
    }

    @Override
    public Class<?> getReturnType() {
        return enumType;
    }

}
