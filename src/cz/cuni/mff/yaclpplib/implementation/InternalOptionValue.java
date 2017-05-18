package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.OptionValue;

/**
 * Internal interface to option values.
 */
interface InternalOptionValue extends OptionValue {

    /**
     * @param tokenList containing at least one token
     */
    void completeValue(TokenList tokenList, ValuePolicy policy);
}
