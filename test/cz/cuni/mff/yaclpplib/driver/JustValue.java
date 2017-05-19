package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.OptionValue;

class JustValue implements OptionValue {
    private final String input;

    public JustValue(String input) {
        this.input = input;
    }

    @Override
    public ArgumentParser getParser() {
        return null;
    }

    @Override
    public String getOption() {
        return null;
    }

    @Override
    public String getValue() {
        return input;
    }

    @Override
    public String[] getRawTokens() {
        return new String[]{input};
    }

    @Override
    public boolean hasValue(){
        return input != null;
    }
}
