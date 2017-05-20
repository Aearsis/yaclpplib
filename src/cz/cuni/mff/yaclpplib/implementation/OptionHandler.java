package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.Options;

import java.lang.reflect.AccessibleObject;

public interface OptionHandler {

    ArgumentParser getParser();

    Class<?> getType();
    String getAnyOptionName();
    ValuePolicy getValuePolicy();
    boolean isMandatory();
    AccessibleObject getHandledObject();
    Options getDefinitionClass();

    String getHelpLine();

    /**
     * Handle the option being found on command line. Note that optionName is not required
     * to be the actual option name used on the command line - e.g. when there are more
     * of them aggregated into an array.
     *
     * @param typedValue
     * @param optionName
     */
    void setValue(Object typedValue, String optionName);

    /**
     * Called after parsing all options given in a single parse.
     */
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
     * @return handler, wrapped if applicable
     */
    static OptionHandler wrap(OptionHandler rawHandler) {
        OptionHandler wrappedHandler = rawHandler;

        // One-dimensional arrays of known types
        if (ArrayOption.isApplicable(wrappedHandler)) {
            wrappedHandler = new ArrayOption(wrappedHandler);
        }

        // Autoboxer for types
        if (BoxedOption.isApplicable(wrappedHandler)) {
            wrappedHandler = new BoxedOption(wrappedHandler);
        }

        // Allows optional value for booleans
        if (BooleanOption.isApplicable(wrappedHandler)) {
            wrappedHandler = new BooleanOption(wrappedHandler);
        }

        // Does range checks on integers/longs
        if (RangeOption.isApplicable(wrappedHandler)) {
            wrappedHandler = new RangeOption(wrappedHandler);
        }

        return wrappedHandler;
    }
}
