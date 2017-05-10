package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.OptionValue;
import cz.cuni.mff.yaclpplib.Options;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

public class FieldOption extends OptionBase {

    final private Field field;

    public FieldOption(Options definitionClass, Field field) {
        super(definitionClass, field);
        this.field = field;
        field.setAccessible(true);
    }

    @Override
    protected void haveTypedValue(OptionValue optionValue, Object typedValue) {
        try {
            field.set(definitionClass, typedValue);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new InternalError();
        }
    }

    @Override
    public Class getType() {
        return field.getType();
    }

    @Override
    public ValuePolicy getValuePolicy() {
        return ValuePolicy.MANDATORY;
    }
}
