package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.OptionValue;

class JustValue implements OptionValue {
    private final String input;

    public JustValue(String input) {
        this.input = input;
    }

    @Override
    public String getName(){
        return "--testoption";
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
