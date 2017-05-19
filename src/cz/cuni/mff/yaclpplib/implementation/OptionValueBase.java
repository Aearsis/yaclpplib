package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.OptionValue;

public abstract class OptionValueBase implements OptionValue {

    private final ArgumentParser parser;

    OptionValueBase(ArgumentParser parser) {
        this.parser = parser;
    }

    @Override
    public ArgumentParser getParser() {
        return parser;
    }
}
