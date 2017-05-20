package cz.cuni.mff.yaclpplib;

public class MissingOptionValue extends InvalidOptionValue {

    private final OptionValue optionValue;

    public MissingOptionValue(OptionValue optionValue) {
        this.optionValue = optionValue;
    }

    @Override
    public String getMessage() {
        return "Parameter " + optionValue.getName() + " must have an associated value.";
    }
}
