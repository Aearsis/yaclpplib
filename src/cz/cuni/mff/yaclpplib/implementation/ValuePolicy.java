package cz.cuni.mff.yaclpplib.implementation;

import java.util.function.Predicate;

public enum ValuePolicy {
    NEVER(value -> false),
    OPTIONAL(value -> !value.startsWith("-")),
    MANDATORY(value -> true);

    private final Predicate<String> valueAcceptor;

    ValuePolicy(Predicate<String> valueAcceptor) {
        this.valueAcceptor = valueAcceptor;
    }

    boolean acceptsValue(String value) {
        return valueAcceptor.test(value);
    }
}
