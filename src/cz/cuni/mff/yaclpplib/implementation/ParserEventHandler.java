package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.InvalidArgumentsException;
import cz.cuni.mff.yaclpplib.InvalidSetupError;
import cz.cuni.mff.yaclpplib.Options;
import cz.cuni.mff.yaclpplib.implementation.options.MethodCall;

import java.lang.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Wrapper for @AfterParse and @BeforeParse methods to bind them with the Options instances.
 */
class ParserEventHandler {

    private final MethodCall<Void> call;

    ParserEventHandler(Method method, Options options, ArgumentParser parser) {
        call = getInvokeMethodCall(method, options, parser);
        for (Class exception : method.getExceptionTypes()) {
            if (!(InvalidArgumentsException.class.isAssignableFrom(exception))
                    || !(RuntimeException.class.isAssignableFrom(exception))) {
                throw new InvalidSetupError("A Before/AfterParse method must throw only runtime / invalid arguments exceptions.");
            }
        }
    }

    static MethodCall<Void> getInvokeMethodCall(Method method, Options options, ArgumentParser parser) {
        SecurityUtility.makeAccessible(method);
        switch (method.getParameterCount()) {
            case 0:
                return (foo) -> method.invoke(options);
            case 1:
                if (method.getParameterTypes()[0].equals(ArgumentParser.class)) {
                    return  (foo) -> method.invoke(options, parser);
                }
                // fall through
            default:
                throw new InvalidSetupError("A Before/AfterParse method can have either no arguments, or just ArgumentParser.");
        }
    }

    void invoke() throws InvalidArgumentsException {
        try {
            call.call(null);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof InvalidArgumentsException) {
                throw ((InvalidArgumentsException) e.getCause());
            }
            if (e.getCause() instanceof RuntimeException) {
                throw ((RuntimeException) e.getCause());
            }
            throw new InternalError("An unexpected type of exception was thrown.");
        } catch (IllegalAccessException e) {
            throw new InternalError("An access was denied to a Before/AfterParse handler.");
        }
    }
}
