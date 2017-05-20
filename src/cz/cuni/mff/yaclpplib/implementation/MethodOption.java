package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.InvalidSetupError;
import cz.cuni.mff.yaclpplib.Options;
import cz.cuni.mff.yaclpplib.annotation.OptionalValue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodOption extends OptionHandlerBase {

    final private Class type;
    final boolean annotatedOptional;

    private interface MethodCall {
        void call(Object typedValue) throws IllegalAccessException, InvocationTargetException;
    }

    MethodCall call;

    public MethodOption(ArgumentParser parser, Options definitionClass, Method method) {
        super(parser, definitionClass, method);
        SecurityUtility.makeAccessible(method);

        annotatedOptional = method.getDeclaredAnnotation(OptionalValue.class) != null;

        switch (method.getParameterCount()) {
            case 0:
                call = ((typedValue) -> method.invoke(definitionClass));
                type = Void.class;
                break;
            case 1:
                Class<?> argType = method.getParameterTypes()[0];
                type = argType;
                call = ((typedValue) -> method.invoke(definitionClass, typedValue));
                break;
            default:
                throw new InvalidSetupError("@Option method must have at most one argument.");
        }

        if (annotatedOptional) {
            if (type.equals(Void.class))
                throw new InvalidSetupError("Methods without arguments cannot have optional value.");
            if (type.isPrimitive())
                throw new InvalidSetupError("Methods with primitive types cannot have optional values.");
            if (type.isArray() && type.getComponentType().isPrimitive())
                throw new InvalidSetupError("Methods with arrays of primitive type cannot have optional values.");
        }
    }

    @Override
    public void setValue(Object typedValue, String optionName) {
        try {
            call.call(typedValue);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new InternalError();
        }
    }

    @Override
    public Class getType() {
        return type;
    }

    @Override
    public ValuePolicy getValuePolicy() {
        if (type == Void.class) {
            return ValuePolicy.NEVER;
        } else if (annotatedOptional) {
            return ValuePolicy.OPTIONAL;
        } else {
            return ValuePolicy.MANDATORY;
        }
    }
}
