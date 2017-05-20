package cz.cuni.mff.yaclpplib.implementation;

public class ShortOptionValue implements InternalOptionValue {

    private final String name;

    private String[] rawTokens;
    private String value;

    public ShortOptionValue(String primaryToken) {
        rawTokens = new String[] { primaryToken };

        if (primaryToken.length() > 2) {
            name = primaryToken.substring(0, 2);
            value = primaryToken.substring(2);
        } else {
            name = primaryToken;
            value = null;
        }
    }

    @Override
    public void completeValue(TokenList tokenList, ValuePolicy policy) {
        if (hasValue())
            return;

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

    public static boolean matches(String optionToken) {
        return optionToken.matches("-[a-zA-Z0-9].*");
    }
}
