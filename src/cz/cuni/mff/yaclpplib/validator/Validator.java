package cz.cuni.mff.yaclpplib.validator;

public interface Validator {

    /**
     * Check the conditions, return true if they're valid.
     * @return whether the condition holds
     */
    boolean isValid();
}
