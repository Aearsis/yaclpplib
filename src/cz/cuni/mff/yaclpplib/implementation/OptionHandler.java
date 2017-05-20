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

}
