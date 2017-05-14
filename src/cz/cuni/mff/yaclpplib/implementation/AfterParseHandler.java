package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.Options;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Wrapper for @AfterParse methods to bind them with the Options instances.
 *
 * TODO: do we want this class public?
 */
class AfterParseHandler {

    private final Method method;
    private final Options options;

    public AfterParseHandler(Method method, Options options) {
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
