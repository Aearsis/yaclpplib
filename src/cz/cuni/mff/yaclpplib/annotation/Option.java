package cz.cuni.mff.yaclpplib.annotation;

import cz.cuni.mff.yaclpplib.implementation.Options;

import java.lang.annotation.*;

/**
 * Sets a short or long option text to go with this attribute
 * or method.
 *
 * This annotation may be used multiple times to allow synonyms.
 */
@Documented
@Repeatable(Options.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Option {
    String value();
}
