package cz.cuni.mff.yaclpplib.implementation;

/**
 * Boolean options are often used without specifing the value, such as --verbose.
 *
 * This decorator is doing the trick.
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
    public void setValue(Object typedValue, String optionName) {
        super.setValue(typedValue != null ? typedValue : true, optionName);
    }

    @Override
    public ValuePolicy getValuePolicy() {
        return ValuePolicy.OPTIONAL;
    }
}
