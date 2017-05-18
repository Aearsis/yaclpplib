package cz.cuni.mff.yaclpplib;

import cz.cuni.mff.yaclpplib.implementation.OptionValueBase;

public class MissingOptionValue extends InvalidOptionValue {

    private final OptionValue optionValue;

    public MissingOptionValue(OptionValue optionValue) {
        this.optionValue = optionValue;
    }

    @Override
    public String getMessage() {
        return "Parameter " + optionValue.getOption() + " must have an associated value.";
    }
}
