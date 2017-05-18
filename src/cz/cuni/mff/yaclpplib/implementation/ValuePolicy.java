package cz.cuni.mff.yaclpplib.implementation;

enum ValuePolicy {
    NEVER, OPTIONAL, MANDATORY;

    boolean eatsValue(String value) {
        if (this == OPTIONAL && value.startsWith("-"))
            return false;
        return this != NEVER;
    }
}
