package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.annotation.Option;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Options {
    Option[] value();
}
