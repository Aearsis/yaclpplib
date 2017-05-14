package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.OptionValue;

// TODO: check if the implementation here is ok and relocate it, if needed, or somehow make abstraction
public class OptionValueBase implements OptionValue {

    private final ArgumentParser parser;
    private String option = null;
    private String value = null;
    private String[] rawTokens;

    OptionValueBase(ArgumentParser parser) {
        this.parser = parser;
    }

    @Override
    public String getOption() {
        return option;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String[] getRawTokens() {
        return rawTokens;
    }

    void setOption(String option) {
        this.option = option;
    }

    void setValue(String value) {
        this.value = value;
    }

    void setRawTokens(String[] rawTokens) {
        this.rawTokens = rawTokens;
    }

    @Override
    public ArgumentParser getParser() {
        return parser;
    }
}
