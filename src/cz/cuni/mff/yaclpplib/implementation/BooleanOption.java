package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.OptionValue;

/**
 * Boolean options are often used without specifing the value, such as --verbose.
 *
 * This decorator is doing half the trick. The other half is in the BooleanDriver, parsing no value as truthy.
 */
class BooleanOption extends OptionHandlerDecorator {

    static boolean isApplicable(OptionHandler handler) {
        return handler.getValuePolicy() == ValuePolicy.MANDATORY &&
                (handler.getType().equals(Boolean.class) || handler.getType().equals(boolean.class));
    }

    BooleanOption(OptionHandler decorated) {
        super(decorated);
    }

    @Override
    public ValuePolicy getValuePolicy() {
        return ValuePolicy.OPTIONAL;
    }
}
