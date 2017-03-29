package cz.cuni.mff.yaclpplib.annotation;

import cz.cuni.mff.yaclpplib.implementation.Options;

import java.lang.annotation.*;

@Repeatable(Options.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Option {
    String value();
}
