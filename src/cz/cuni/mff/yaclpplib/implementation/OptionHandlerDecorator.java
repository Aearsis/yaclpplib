package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.Options;

import java.lang.reflect.AccessibleObject;

/**
 * Base class for decorating OptionHandlers.
 */
abstract public class OptionHandlerDecorator implements OptionHandler {

    protected final OptionHandler decorated;

    public OptionHandlerDecorator(OptionHandler decorated) {
        this.decorated = decorated;
    }

    @Override
    public void setValue(Object typedValue, String optionName) {
        decorated.setValue(typedValue, optionName);
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
    public ArgumentParser getParser() {
        return decorated.getParser();
    }

    @Override
    public String getAnyOptionName() {
        return decorated.getAnyOptionName();
    }
}
