package cz.cuni.mff.yaclpplib.validator;

@FunctionalInterface
public interface ExceptionFactory<T> {
    T create();
}
