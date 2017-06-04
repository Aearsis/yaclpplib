package cz.cuni.mff.yaclpplib.implementation.options;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.Options;
import cz.cuni.mff.yaclpplib.implementation.InternalError;
import cz.cuni.mff.yaclpplib.implementation.SecurityUtility;
import cz.cuni.mff.yaclpplib.implementation.ValuePolicy;

import java.lang.reflect.Field;

/**
 * Represents a single field in an {@link Options} instance.
 */
public class FieldOption extends MemberOptionHandler {

    final private Field field;

    public FieldOption(ArgumentParser parser, Options definitionClass, Field field) {
        super(parser, definitionClass, field);
        this.field = field;
        SecurityUtility.makeAccessible(field);
    }

    @Override
    public void setValue(Object typedValue, String optionName) throws InvalidOptionValue {
        try {
            field.set(getDefinitionClass(), typedValue);
        } catch (IllegalAccessException e) {
            throw new InternalError();
        } catch (IllegalArgumentException e) {
            throw new InvalidOptionValue("A value of " + typedValue + " is invalid for " + optionName + ".");
        }
    }

    @Override
    public Class getType(){
        return field.getType();
    }

    @Override
    public ValuePolicy getValuePolicy() {
        return ValuePolicy.MANDATORY;
    }

}
