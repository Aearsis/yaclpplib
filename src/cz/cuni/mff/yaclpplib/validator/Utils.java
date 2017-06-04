package cz.cuni.mff.yaclpplib.validator;

import cz.cuni.mff.yaclpplib.InvalidArgumentsException;

import java.util.Arrays;

public final class Utils {

    /**
     * Verifies that when the first validator is evaluated as true, the second one also returns true
     * @param a validator for first group of options
     * @param b validator for second group of options
     * @return true if either a is evaluated as false, or both a and b are true
     */
    public static Validator imply(Validator a, Validator b) {
        return () -> !a.isValid() || b.isValid();
    }

    /**
     * Ensures that when the second option is present, the first one is present too.
     * @param a validator for the group of options which is depended upon
     * @param b validator for the group depending on a
     * @return true, if either b is evaluated as false, or both a and b are true
     */
    public static Validator depends(Validator a, Validator b) {
        return imply(b, a);
    }

    /**
     * Checks that both a and b evaluate to the same value
     * @param a validator for first group of options
     * @param b validator for second group of options
     * @return true if both a and b return the same value
     */
    public static Validator same(Validator a, Validator b) {
        return () -> a.isValid() == b.isValid();
    }

    /**
     * Checks whether all the supplied validators evaluate as true
     * @param validators an array of checked validators
     * @return true if all validators evaluate as true
     */
    public static Validator all(Validator... validators) {
        return () -> Arrays.stream(validators).allMatch(Validator::isValid);
    }

    /**
     * Checks whether none the supplied validators evaluate as true
     * @param validators an array of checked validators
     * @return true if no validator evaluates as true
     */
    public static Validator none(Validator... validators) {
        return () -> Arrays.stream(validators).noneMatch(Validator::isValid);
    }

    /**
     * Checks if at least one of the supplied validators evaluates as true
     * @param validators an array of checked validators
     * @return true if at least one of validators is true
     */
    public static Validator any(Validator... validators) {
        return () -> Arrays.stream(validators).anyMatch(Validator::isValid);
    }

    /**
     * Checks if at most one of the supplied validators evaluates as true
     * @param validators an array of checked validators
     * @return true if at most one of validators is true
     */
    public static Validator atMostOne(Validator... validators) {
        return () -> Arrays.stream(validators).filter(Validator::isValid).count() <= 1;
    }

    /**
     * Checks if at most one of the supplied validators evaluates as true
     * @param validators an array of checked validators
     * @return true if at most one of validators is true
     */
    public static Validator exactlyOne(Validator... validators) {
        return () -> Arrays.stream(validators).filter(Validator::isValid).count() == 1;
    }

    /**
     * Checks if the validator evaluates as false
     * @param a checked validator
     * @return true, if a returns false
     */
    public static Validator not(Validator a) {
        return () -> !a.isValid();
    }

    /**
     * Always returns false
     * @return false
     */
    public static Validator never() {
        return () -> false;
    }

    /**
     * Always returns true
     * @return true
     */
    public static Validator always() {
        return () -> true;
    }

    public static ExceptionFactory<InvalidArgumentsException> message(String message) {
        return () -> new InvalidArgumentsException(message);
    }

}

