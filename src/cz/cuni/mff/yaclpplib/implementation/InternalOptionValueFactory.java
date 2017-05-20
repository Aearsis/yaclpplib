package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.OptionValue;

import java.util.Optional;

final class InternalOptionValueFactory {

    private InternalOptionValueFactory() {
        throw new AssertionError();
    }

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
