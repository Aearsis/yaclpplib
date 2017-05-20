package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.Options;

import java.lang.reflect.Field;

public class FieldOption extends OptionHandlerBase {

    final private Field field;

    public FieldOption(ArgumentParser parser, Options definitionClass, Field field) {
        super(parser, definitionClass, field);
        this.field = field;
        SecurityUtility.makeAccessible(field);
    }

    @Override
    public void setValue(Object typedValue, String optionName) {
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
