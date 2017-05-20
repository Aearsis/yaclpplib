package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.OptionValue;
import cz.cuni.mff.yaclpplib.Options;
import cz.cuni.mff.yaclpplib.driver.Driver;

import java.lang.reflect.AccessibleObject;
import java.util.List;

public interface OptionHandler {
    Class<?> getType();
    ValuePolicy getValuePolicy();
    String getHelpLine();
    boolean isMandatory();
    AccessibleObject getHandledObject();
    Options getDefinitionClass();
    List<Class> getDecorators();

    void optionFound(OptionValue optionValue, Driver driver);
    void haveTypedValue(OptionValue optionValue, Object typedValue);
    void finish();

    /**
     * Some combinations have different semantics. Because these generate too much combinations,
     * we handle them using decorators over the handlers.
     *
     * For now, we handle:
     *      - arrays, by aggregating the component type, yielding the final array at the end
     *      - boolean options, because --verbose is a shorthand for "--verbose true"
     *      - primitive types, because they often require special handling
     *      - mandatory options, because we need to track if we encountered them
     *      - range options, we need to check if the value is in the given range
     *
     * @param rawHandler a handler to be wrapped
     * @return handler, wraBoxingOptionpped if applicable
     */
    static OptionHandler wrap(OptionHandler rawHandler) {
        // One-dimensional arrays of known types
        if (ArrayOption.isApplicable(rawHandler)) {
            return wrap(new ArrayOption(rawHandler));
        }

        if (BoxedOption.isApplicable(rawHandler)) {
            return wrap(new BoxedOption(rawHandler));
        }

        if (BooleanOption.isApplicable(rawHandler)) {
            return wrap(new BooleanOption(rawHandler));
        }

        if (MandatoryOption.isApplicable(rawHandler)) {
            return wrap(new MandatoryOption(rawHandler));
        }

        if (RangeOption.isApplicable(rawHandler)) {
            return wrap(new RangeOption(rawHandler));
        }

        return rawHandler;
    }
}
