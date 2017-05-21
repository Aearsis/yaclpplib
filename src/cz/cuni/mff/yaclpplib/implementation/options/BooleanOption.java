package cz.cuni.mff.yaclpplib.implementation.options;

import cz.cuni.mff.yaclpplib.implementation.OptionHandler;
import cz.cuni.mff.yaclpplib.implementation.ValuePolicy;

/**
 * <p>Boolean options are often used without specifying the value, such as --verbose.</p>
 *
 * <p>This decorator is doing the trick. </p>
 *
 * <p>Valid, behaving-as-expected variants: </p>
 * <ul>
 * <li>--verbose</li>
 * <li>-q</li>
 * <li>--verbose=true</li>
 * <li>-qfalse</li>
 * </ul>
 * <p>Valid, but not doing what would you expect:</p>
 * <ul>
 * <li>--verbose true</li>
 * <li>-q false</li>
 * </ul>
 * <p>These take the second argument as a positional argument instead.</p>
 */
public class BooleanOption extends OptionHandlerDecorator {

    BooleanOption(OptionHandler decorated) {
        super(decorated);
    }

    @Override
    public void setValue(Object typedValue, String optionName) {
        super.setValue(typedValue != null ? typedValue : true, optionName);
    }

    @Override
    public ValuePolicy getValuePolicy(){
        return ValuePolicy.IFNEEDBE;
    }

    private static boolean isApplicable(OptionHandler handler) {
        return handler.getValuePolicy() == ValuePolicy.MANDATORY && handler.getType().equals(Boolean.class);
    }

    /**
     * Wraps the handler into {@link BooleanOption} if needed
     * @param handler handler to wrap
     * @return wrapped handler, if needed, otherwise the original one
     */
    public static OptionHandler wrapIfApplicable(OptionHandler handler) {
        if (BooleanOption.isApplicable(handler)) {
            return new BooleanOption(handler);
        }
        else {
            return handler;
        }
    }
}
