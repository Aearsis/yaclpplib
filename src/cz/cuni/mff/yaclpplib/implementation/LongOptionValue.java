package cz.cuni.mff.yaclpplib.implementation;

/**
 * An option value for long option value, such as --all.
 */
public class LongOptionValue implements InternalOptionValue {

    private final String name;

    private String value;
    private String[] rawTokens;

    LongOptionValue(String primaryToken) {
        final String[] split = primaryToken.split("=", 2);

        name = split[0];
        rawTokens = new String[] { primaryToken };

        if (split.length > 1) {
            value = split[1];
        }
    }

    @Override
    public void completeValue(TokenList tokenList, ValuePolicy policy) {
        if (hasValue()) {
            return;
        }

        final String possibleValue = tokenList.peek();
        if (policy.acceptsValue(possibleValue)) {
            value = tokenList.remove();
            rawTokens = new String[] { name, value };
        }
    }

    @Override
    public String getName() {
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

    /**
     * Checks if the option token can be handled by this long option value
     * @param optionToken read token
     * @return true, if this class can process the token
     */
    public static boolean matches(String optionToken) {
        return optionToken.matches("--[a-zA-Z0-9].*");
    }
}
