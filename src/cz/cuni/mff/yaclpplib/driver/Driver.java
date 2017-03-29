package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.OptionValue;
import cz.cuni.mff.yaclpplib.InvalidOptionValue;

/**
 * Driver for a type of argument.
 */
public interface Driver<T extends Object> {

    T parse(OptionValue x) throws InvalidOptionValue;

    /**
     * As a limitation of Java, we need to know the type at runtime,
     * but generic arguments are stripped at compilation time.
     * @return T.class
     */
    Class getReturnType();


}
