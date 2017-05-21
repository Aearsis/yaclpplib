package cz.cuni.mff.yaclpplib.implementation;

import java.util.function.Predicate;

/**
 * Represents how the library behave to a specific field or method.
 */
public enum ValuePolicy {
    /**
     * The member never has any value.
     */
    NEVER(value -> false),
    /**
     * The member is a boolean option, which can have a value (--verbose=false), but do not accept it as another token.
     */
    IFNEEDBE(value -> false),
    /**
     * The member may have a value, we need to decide depending on the input
     */
    OPTIONAL(value -> !value.startsWith("-")),
    /**
     * The member must have a value, missing value is wrong.
     */
    MANDATORY(value -> true);

    private final Predicate<String> valueAcceptor;

    ValuePolicy(Predicate<String> valueAcceptor) {
        this.valueAcceptor = valueAcceptor;
    }

    /**
     * Checks if the policy accepts a given value
     * @param value read string value
     * @return true if the value is mandatory or optional and the value is accepted, otherwise false
     */
    boolean acceptsValue(String value) {
        return valueAcceptor.test(value);
    }
}
