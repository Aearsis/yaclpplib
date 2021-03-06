package cz.cuni.mff.yaclpplib.implementation.options;

import cz.cuni.mff.yaclpplib.*;
import cz.cuni.mff.yaclpplib.implementation.OptionHandler;
import cz.cuni.mff.yaclpplib.implementation.ValuePolicy;

/**
 * Abstract base class for decorating OptionHandlers.
 */
abstract public class OptionHandlerDecorator implements OptionHandler {

    final OptionHandler decorated;

    OptionHandlerDecorator(OptionHandler decorated) {
        this.decorated = decorated;
    }

    @Override
    public void setValue(Object typedValue, String optionName) throws IllegalOptionValue, InvalidOptionValue {
        decorated.setValue(typedValue, optionName);
    }

    @Override
    public void finish() throws InvalidArgumentsException {
        decorated.finish();
    }

    @Override
    public Class getType() {
        return decorated.getType();
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
