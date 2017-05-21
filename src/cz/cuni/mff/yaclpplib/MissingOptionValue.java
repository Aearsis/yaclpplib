package cz.cuni.mff.yaclpplib;

/**
 * Exception thrown by the library when an option requiring a value doesn't have
 * a value in the given argument list.
 */
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
