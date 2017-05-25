package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.InvalidSetupError;
import cz.cuni.mff.yaclpplib.Options;
import cz.cuni.mff.yaclpplib.implementation.options.MethodCall;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Wrapper for @AfterParse and @BeforeParse methods to bind them with the Options instances.
 */
class ParserEventHandler {

    private final MethodCall<ArgumentParser> call;

    ParserEventHandler(Method method, Options options) {
        SecurityUtility.makeAccessible(method);

        switch (method.getParameterCount()) {
            case 0:
                call = (parser) -> method.invoke(options);
                break;
            case 1:
                if (method.getParameterTypes()[0].equals(ArgumentParser.class)) {
                    call = (parser) -> method.invoke(options, parser);
                    break;
                }
                // fall through
            default:
                throw new InvalidSetupError("A Before/AfterParse method can have either no arguments, or just ArgumentParser.");
        }

        if (method.getExceptionTypes().length > 0) {
            for (Class exception : method.getExceptionTypes()) {
                if (!(RuntimeException.class.isAssignableFrom(exception))) {
                    throw new InvalidSetupError("A Before/AfterParse method must throw only runtime exceptions.");
                }
            }
        }
    }

    void invoke(ArgumentParser parser) {
        try {
            call.call(parser);
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
