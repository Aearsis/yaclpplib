package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.OptionValue;

// TODO: check if the implementation here is ok and relocate it, if needed, or somehow make abstraction
public abstract class OptionValueBase implements InternalOptionValue {

    private final ArgumentParser parser;

    OptionValueBase(ArgumentParser parser) {
        this.parser = parser;
    }

    @Override
    public ArgumentParser getParser() {
        return parser;
    }
}
