package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.MissingOptionValue;
import cz.cuni.mff.yaclpplib.OptionValue;
import cz.cuni.mff.yaclpplib.Options;
import cz.cuni.mff.yaclpplib.driver.Driver;

import java.lang.reflect.AccessibleObject;
import java.util.List;

/**
 * Base class for decorating OptionHandlers.
 */
abstract public class OptionHandlerDecorator implements OptionHandler {

    protected final OptionHandler decorated;

    public OptionHandlerDecorator(OptionHandler decorated) {
        this.decorated = decorated;
    }

    @Override
    public void haveTypedValue(OptionValue optionValue, Object typedValue) {
        decorated.haveTypedValue(optionValue, typedValue);
    }

    @Override
    public void finish() {
        decorated.finish();
    }

    @Override
    public Class getType() {
        return decorated.getType();
    }

    @Override
    public boolean isMandatory() {
        return decorated.isMandatory();
    }

    @Override
    public ValuePolicy getValuePolicy() {
        return decorated.getValuePolicy();
    }

    @Override
    public String getHelpLine() {
        return decorated.getHelpLine();
    }

    @Override
    public AccessibleObject getHandledObject() {
        return decorated.getHandledObject();
    }

    @Override
    public Options getDefinitionClass() {
        return decorated.getDefinitionClass();
    }

    @Override
    public void optionFound(OptionValue optionValue, Driver driver) {
        final Object typedValue = driver.parse(optionValue);
        haveTypedValue(optionValue, typedValue);
    }
}
