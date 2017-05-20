package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.InvalidSetupError;
import cz.cuni.mff.yaclpplib.annotation.Range;

/**
 * Handles checking if a mandatory option was encountered.
 */
class RangeOption extends OptionHandlerDecorator {

    private long minimumValue;
    private long maximumValue;
    private String firstName;

    static boolean isApplicable(OptionHandler handler) {
        return handler.getHandledObject().getDeclaredAnnotation(Range.class) != null;
    }

    RangeOption(OptionHandler decorated) {
        super(decorated);

        if (!(getType().equals(Integer.class) || getType().equals(Long.class))) {
            throw new InvalidSetupError("The Range annotation can be used only with long or int types.");
        }

        Range range = getHandledObject().getDeclaredAnnotation(Range.class);
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
            throw new InvalidOptionValue("Value of option " + optionName
                    + " must be between " + minimumValue + " and " + maximumValue + ".");
        }

        super.setValue(typedValue, optionName);
    }
}
