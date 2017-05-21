package cz.cuni.mff.yaclpplib.implementation;

/**
 * An option value of short options, such as -a.
 */
public class ShortOptionValue implements InternalOptionValue {

    private final String name;

    private String[] rawTokens;
    private String value;

    /**
     * Processes first token. If it's long enough, splits it into
     * option and value.
     * @param primaryToken primary option token
     */
    public ShortOptionValue(String primaryToken) {
        rawTokens = new String[] { primaryToken };

        if (primaryToken.length() > 2) {
            // This means we have something like -avalue, so we split it into '-a' 'value'
            name = primaryToken.substring(0, 2);
            value = primaryToken.substring(2);
        } else {
            // No value, just have the token
            name = primaryToken;
            value = null;
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
    public String getName(){
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
     * Checks if the option token can be handled by this short option value
     * @param optionToken read token
     * @return true, if this class can process the token
     */
    public static boolean matches(String optionToken) {
        return optionToken.matches("-[a-zA-Z0-9].*");
    }
}
