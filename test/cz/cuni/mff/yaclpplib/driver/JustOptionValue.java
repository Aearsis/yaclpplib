package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.OptionValue;

class JustOptionValue implements OptionValue {
    private final String input;

    public JustOptionValue(String input) {
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
}
