package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.OptionValue;

public class CharacterDriver implements Driver<Character> {

    @Override
    public Character parse(OptionValue x) throws InvalidOptionValue {
        if (x.getValue().length() != 1) {
            throw new InvalidOptionValue("Option " + x.getName() + " must be a single character.");
        }
        return x.getValue().charAt(0);
    }

    @Override
    public Class<Character> getReturnType() {
        return Character.class;
    }
}
