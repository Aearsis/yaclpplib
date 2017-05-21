package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.InvalidSetupError;

import java.lang.reflect.*;

/**
 * An utility class for exposing fields and methods.
 * This allows us to access private or protected methods our users annotate.
 */
public class SecurityUtility {

    /**
     * Make a Method or Field accessible through reflection,
     * even though it is private, protected or package-protected.<br/>
     *
     * This can be blocked by the SecurityManager, but when the library cannot access the member object,
     * it cannot fulfill its duty.
     *
     * @param object a Field or Method to make accessible
     * @throws InvalidSetupError When this member cannot be accessed.
     */
    public static <T extends AccessibleObject & Member> void makeAccessible(T object) throws InvalidSetupError {
        if (object.isAccessible()) {
            return;
        }

        try {
            object.setAccessible(true);
        } catch (SecurityException e) {
            throw new InvalidSetupError("@Option members must be accessible for the library.");
        }
    }
}
