package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.InvalidSetupError;

import java.lang.reflect.*;

class SecurityUtility {

    /**
     * Make a Method or Field accessible through reflection,
     * even though it is private, protected or package-protected.
     *
     * This can be blocked by the SecurityManager, but when the library cannot access the member object,
     * it cannot fulfill its duty.
     * @param object a Field or Method to make accessible
     * @throws InvalidSetupError When this member cannot be accessed.
     */
    static <T extends AccessibleObject & Member> void makeAccessible(T object) throws InvalidSetupError {
        int modifiers = object.getModifiers();
        if (Modifier.isPublic(modifiers))
            return;

        try {
            object.setAccessible(true);
        } catch (SecurityException e) {
            throw new InvalidSetupError("@Option members must be accessible for the library.");
        }
    }
}
