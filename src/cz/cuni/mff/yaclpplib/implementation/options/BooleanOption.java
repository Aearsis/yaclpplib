package cz.cuni.mff.yaclpplib.implementation.options;

import cz.cuni.mff.yaclpplib.implementation.OptionHandler;
import cz.cuni.mff.yaclpplib.implementation.ValuePolicy;

/**
 * Boolean options are often used without specifying the value, such as --verbose.
 *
 * This decorator is doing the trick.
 */
public class BooleanOption extends OptionHandlerDecorator {

    public static boolean isApplicable(OptionHandler handler) {
        return handler.getValuePolicy() == ValuePolicy.MANDATORY && handler.getType().equals(Boolean.class);
    }

    public BooleanOption(OptionHandler decorated) {
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

    public static OptionHandler wrapIfApplicable(OptionHandler handler) {
        if (BooleanOption.isApplicable(handler)) {
            return new BooleanOption(handler);
        } else {
            return handler;
        }
    }
}
