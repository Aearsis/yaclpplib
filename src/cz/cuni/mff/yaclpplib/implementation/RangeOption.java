package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.IllegalOptionValue;
import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.InvalidSetupError;
import cz.cuni.mff.yaclpplib.annotation.Range;

import java.lang.reflect.AccessibleObject;

/**
 * Handles checking if a mandatory option was encountered.
 */
class RangeOption extends OptionHandlerDecorator {

    private long minimumValue;
    private long maximumValue;

    static OptionHandler wrapIfApplicable(OptionHandler handler, AccessibleObject member) {
        if (member.getDeclaredAnnotation(Range.class) != null) {
            return new RangeOption(handler, member.getDeclaredAnnotation(Range.class));
        }
        else {
            return handler;
        }
    }

    RangeOption(OptionHandler decorated, Range range) {
        super(decorated);

        if (!(getType().equals(Integer.class) || getType().equals(Long.class))) {
            throw new InvalidSetupError("The Range annotation can be used only with long or int types.");
        }

        minimumValue = range.minimumValue();
        maximumValue = range.maximumValue();
    }

    @Override
    public void setValue(Object typedValue, String optionName) {
        long value;
        if (getType().equals(Integer.class)) {
            value = (Integer) typedValue;
        } else {
            value = (Long) typedValue;
        }

        if (value < minimumValue || value > maximumValue) {
            throw new IllegalOptionValue("Value of option " + optionName
                    + " must be between " + minimumValue + " and " + maximumValue + ".");
        }

        super.setValue(typedValue, optionName);
    }
}
