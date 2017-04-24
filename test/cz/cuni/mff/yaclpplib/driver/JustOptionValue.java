package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.implementation.OptionValueBase;

class JustOptionValue extends OptionValueBase {
    private final String input;

    public JustOptionValue(String input) {
        super(null);
        this.input = input;
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
