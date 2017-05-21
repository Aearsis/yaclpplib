package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.InvalidSetupError;
import cz.cuni.mff.yaclpplib.Options;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Wrapper for @AfterParse methods to bind them with the Options instances.
 */
class AfterParseHandler {

    private final Method method;
    private final Options options;

    public AfterParseHandler(Method method, Options options) {
        SecurityUtility.makeAccessible(method);
        if (method.getParameterCount() != 0) {
            throw new InvalidSetupError("An AfterParse method cannot have any parameters.");
        }
        if (method.getExceptionTypes().length > 0) {
            for (Class exception : method.getExceptionTypes()) {
                if (!(RuntimeException.class.isAssignableFrom(exception))) {
                    throw new InvalidSetupError("An AfterParse method must throw only runtime exceptions.");
                }
            }
        }
        this.method = method;
        this.options = options;
    }

    public void invoke() {
        try {
            method.invoke(options);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException) {
                throw ((RuntimeException) e.getCause());
            }
            // User is expected to use only runtime exceptions, others don't matter
        } catch (IllegalAccessException e) {
            // This shouldn't happen
            e.printStackTrace();
            throw new InternalError();
        }
    }
}
