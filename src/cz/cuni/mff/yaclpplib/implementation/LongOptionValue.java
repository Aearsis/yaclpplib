package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.ArgumentParser;

import java.util.ArrayList;
import java.util.List;

public class LongOptionValue extends OptionValueBase {

    private final String name;

    private String value;
    private String[] rawTokens;

    public static boolean matches(String optionToken) {
        return optionToken.matches("--[a-zA-Z0-9].*");
    }

    LongOptionValue(ArgumentParser parser, String primaryToken) {
        super(parser);

        final String[] split = primaryToken.split("=", 2);

        name = split[0];
        rawTokens = new String[] { primaryToken };

        if (split.length > 1) {
            value = split[1];
        }
    }

    @Override
    public void completeValue(TokenList tokenList, ValuePolicy policy) {
        /*
         * It would be nice, if we could parse also --option with_value, but that is too ambiguous.
         * If you really want, and accept that --verbose file results in "file is not true/false",
         * you can implement it here.
         */
    }

    @Override
    public String getOption() {
        return name;
    }

    @Override
    public boolean hasValue() {
        return value != null;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String[] getRawTokens() {
        return rawTokens;
    }
}
