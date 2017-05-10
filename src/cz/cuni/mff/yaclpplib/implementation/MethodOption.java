package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.InvalidSetupError;
import cz.cuni.mff.yaclpplib.OptionValue;
import cz.cuni.mff.yaclpplib.Options;
import cz.cuni.mff.yaclpplib.annotation.OptionalValue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodOption extends OptionBase {

    final private Class type;
    final boolean optional;


    private interface MethodCall {
        void call(OptionValue value, Object typedValue) throws IllegalAccessException, InvocationTargetException;
    }

    MethodCall call;

    public MethodOption(Options definitionClass, Method method) {
        super(definitionClass, method);
        method.setAccessible(true);

        optional = method.getDeclaredAnnotation(OptionalValue.class) != null;

        switch (method.getParameterCount()) {
            case 0:
                call = ((value, typedValue) -> method.invoke(definitionClass));
                type = Void.class;
                break;
            case 1:
                Class<?> argType = method.getParameterTypes()[0];
                if (argType.equals(OptionValue.class)) {
                    type = Void.class;
                    call = ((value, typedValue) -> method.invoke(definitionClass, value));
                } else {
                    type = argType;
                    call = ((value, typedValue) -> method.invoke(definitionClass, typedValue));
                }
                break;
            case 2:
                Class<?>[] argTypes = method.getParameterTypes();
                if (method.getParameterTypes()[0].equals(OptionValue.class)) {
                    call = (value, typedValue) -> method.invoke(definitionClass, value, typedValue);
                    type = argTypes[1];
                } else if (method.getParameterTypes()[1].equals(OptionValue.class)) {
                    call = (value, typedValue) -> method.invoke(definitionClass, typedValue, value);
                    type = argTypes[0];
                } else {
                    throw new InvalidSetupError("@Option method with two arguments must have one of them OptionValue.");
                }
                break;
            default:
                throw new InvalidSetupError("@Option method must have specific arguments - please consult the manual.");
        }

        if (optional) {
            if (type.isPrimitive())
                throw new InvalidSetupError("Methods with primitive types cannot have optional values.");
            if (type.isArray() && type.getComponentType().isPrimitive())
                throw new InvalidSetupError("Methods with arrays of primitive type cannot have optional values.");
        }
    }

    @Override
    protected void haveTypedValue(OptionValue optionValue, Object typedValue) {
        try {
            call.call(optionValue, typedValue);
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
        } else if (optional) {
            return ValuePolicy.OPTIONAL;
        } else {
            return ValuePolicy.MANDATORY;
        }
    }
}
