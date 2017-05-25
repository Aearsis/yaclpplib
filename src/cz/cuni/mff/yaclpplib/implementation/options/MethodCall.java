package cz.cuni.mff.yaclpplib.implementation.options;

import java.lang.reflect.InvocationTargetException;

@FunctionalInterface
public interface MethodCall<T> {
    void call(T typedValue) throws IllegalAccessException, InvocationTargetException;
}
