package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.Options;

public interface OptionHandler {

    ArgumentParser getParser();

    Class<?> getType();
    String getAnyOptionName();
    ValuePolicy getValuePolicy();
    Options getDefinitionClass();

    String getHelpLine();

    /**
     * Handle the option being found on command line. Note that optionName is not required
     * to be the actual option name used on the command line - e.g. when there are more
     * of them aggregated into an array.
     *
     * @param typedValue a value of option, in the type the option wants
     * @param optionName any name of the option (for error messages)
     */
    void setValue(Object typedValue, String optionName);

    /**
     * Called after parsing all options given in a single parse.
     */
    void finish();

}
