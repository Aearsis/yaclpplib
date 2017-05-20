package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.MissingMandatoryOptionException;
import cz.cuni.mff.yaclpplib.OptionValue;
import cz.cuni.mff.yaclpplib.annotation.Option;

/**
 * Handles checking if a mandatory option was encountered.
 */
class MandatoryOption extends OptionHandlerDecorator {

    private boolean encountered = false;

    static boolean isApplicable(OptionHandler handler) {
        return !(handler.getDecorators().contains(MandatoryOption.class)) && handler.isMandatory();
    }

    MandatoryOption(OptionHandler decorated) {
        super(decorated);
    }

    @Override
    public void haveTypedValue(OptionValue optionValue, Object typedValue) {
        encountered = true;

        decorated.haveTypedValue(optionValue, typedValue);
    }

    @Override
    public void finish() {
        if (!encountered) {
            throw new MissingMandatoryOptionException(new String[] {
                    getHandledObject().getAnnotationsByType(Option.class)[0].value()
            });
        }
        super.finish();
    }
}
