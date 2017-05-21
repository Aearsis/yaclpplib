package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.OptionValue;
import cz.cuni.mff.yaclpplib.implementation.InternalError;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Automagic driver for everything that has a constructor with a single parameter, a String. <br/>
 *
 * This is used (among others) for numeric types.
 */
public class GenericStringConstructableDriver implements Driver {

    final private Class<?> returnType;
    final private Constructor<?> constructor;

    /**
     * Checks if the type has a constructor with exactly 1 argument taking a String
     * @param type checked type
     * @return true if type has a constructor type(String)
     */
    public static boolean hasStringConstructor(Class<?> type) {
        for (Constructor<?> constructor : type.getConstructors()) {
            if (constructor.getParameterCount() == 1 && constructor.getParameterTypes()[0].equals(String.class)) {
                return true;
            }
        }
        return false;
    }

    public GenericStringConstructableDriver(Class<?> returnType) {
        this.returnType = returnType;
        assert hasStringConstructor(returnType);

        try {
            constructor = returnType.getConstructor(String.class);
        } catch (NoSuchMethodException e) {
            // hasStringConstructor should be called before trying to create this driver.
            throw new InternalError();
        }
    }

    @Override
    public Object parse(OptionValue x) throws InvalidOptionValue {
        try {
            return constructor.newInstance(x.getValue());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new InternalError("The constructor was accessible in the setup time.");
        } catch (InvocationTargetException e) {
            throw new InvalidOptionValue("'" + x.getValue() + " is not a valid value: " + e.getCause().getMessage(), e.getCause());
        }
    }

    @Override
    public Class getReturnType() {
        return returnType;
    }
}
