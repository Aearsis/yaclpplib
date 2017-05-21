package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.OptionValue;

/**
 * Internal interface to option values.
 */
interface InternalOptionValue extends OptionValue {

    /**
     * Adds the value to the instance, if it is needed and still missing.
     * @param tokenList containing at least one token
     */
    void completeValue(TokenList tokenList, ValuePolicy policy);
}
