package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.*;

public interface OptionHandler {

    /**
     * Returns an instance of the parser, which created this handler.
     * @return creator of the handler
     */
    ArgumentParser getParser();

    /**
     * Returns the type of handled field or the type of the argument of handled method.
     * @return type of handled member
     */
    Class<?> getType();

    /**
     * Returns one of the option names given to this field or method.
     * @return one of the option names
     */
    String getAnyOptionName();

    /**
     * Returns the value policy of the field or method.
     * @return value policy
     */
    ValuePolicy getValuePolicy();

    /**
     * Gets the instance of an {@link Options} class, where was the represented field or method defined.
     * @return instance of {@link Options} with this member
     */
    Options getDefinitionClass();

    /**
     * Gets a formatted help line for the given option, with all the synonyms includes.
     * @return help line for the option
     */
    String getHelpLine();

    /**
     * Handle the option being found on command line. Note that optionName is not required
     * to be the actual option name used on the command line - e.g. when there are more
     * of them aggregated into an array.
     *
     * @param typedValue a value of option, in the type the option wants
     * @param optionName any name of the option (for error messages)
     */
    void setValue(Object typedValue, String optionName) throws IllegalOptionValue, InvalidOptionValue;

    /**
     * Called after parsing all options given in a single parse.
     */
    void finish() throws InvalidArgumentsException;

}
