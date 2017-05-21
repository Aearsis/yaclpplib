package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.InvalidSetupError;
import cz.cuni.mff.yaclpplib.OptionValue;
import cz.cuni.mff.yaclpplib.annotation.CaseSensitive;

import java.util.HashMap;
import java.util.Map;

/**
 * Driver for enum subclasses, that are not annotated by {@link CaseSensitive} annotation.
 * It is to be created dynamically for every enum subtype encountered in users' Options.
 */
@SuppressWarnings("unchecked")
public class GenericCaseInsensitiveEnumDriver implements Driver {

    final private Class enumType;
    final private Map<String, Object> enumValues = new HashMap<>();

    public GenericCaseInsensitiveEnumDriver(Class enumType) {
        this.enumType = enumType;

        for (Object constant : enumType.getEnumConstants()) {
            final String key = constant.toString().toLowerCase();
            if (enumValues.containsKey(key))
                throw new InvalidSetupError("Enum " + enumType.getTypeName() + " must be case sensitive (annotate with @CaseSensitive).");

            enumValues.put(key, constant);
        }
    }

    @Override
    public Object parse(OptionValue optionValue) throws InvalidOptionValue {
        String value = optionValue.getValue().toLowerCase();
        if (enumValues.containsKey(value)) {
            return enumValues.get(value);
        }
        else {
            throw new InvalidOptionValue("Invalid value for " + optionValue.getName());
        }
    }

    @Override
    public Class<?> getReturnType() {
        return enumType;
    }

}
