package cz.cuni.mff.yaclpplib.implementation;

import java.util.Optional;
import cz.cuni.mff.yaclpplib.OptionValue;

/**
 * Factory which creates the correct {@link OptionValue} depending on token.
 */
final class InternalOptionValueFactory {

    private InternalOptionValueFactory() {
        throw new AssertionError();
    }

    /**
     * Tries to create an {@link InternalOptionValue} instance if there
     * exists a class willing to consume the token.
     * If there is none, returns empty optional.
     * @param optionToken read option token
     * @return an Optional possibly containing {@link InternalOptionValue} instance
     */
    static Optional<InternalOptionValue> tryCreate(String optionToken) {
        if (LongOptionValue.matches(optionToken)) {
            return Optional.of(new LongOptionValue(optionToken));
        }
        else if (ShortOptionValue.matches(optionToken)) {
            return Optional.of(new ShortOptionValue(optionToken));
        }
        return Optional.empty();
    }

}
