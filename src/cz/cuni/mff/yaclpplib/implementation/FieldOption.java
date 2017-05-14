package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.OptionValue;
import cz.cuni.mff.yaclpplib.Options;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

public class FieldOption extends OptionBase {

    final private Field field;
    final private ValuePolicy policy;
    final private Class type;

    public FieldOption(Options definitionClass, Field field) {
        super(definitionClass, field);
        this.field = field;
        this.type = autoBox(field.getType());
        if(field.getType().equals(Boolean.TYPE)) {
            policy = ValuePolicy.OPTIONAL;
        }
        else {
            policy = ValuePolicy.MANDATORY;
        }
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    @Override
    protected void haveTypedValue(OptionValue optionValue, Object typedValue) {
        try {
            if (typedValue == null && field.getType().equals(Boolean.TYPE)) {
                field.set(definitionClass, true);
            }
            else {
                field.set(definitionClass, typedValue);
            }
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new InternalError();
        }
    }

    @Override
    public Class getType() {
        return type;
    }

    @Override
    public ValuePolicy getValuePolicy() {
        return policy;
    }
}
