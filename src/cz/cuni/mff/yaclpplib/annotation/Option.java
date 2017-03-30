package cz.cuni.mff.yaclpplib.annotation;

import java.lang.annotation.*;

/**
 * Sets a short or long option text to go with this attribute
 * or method. Each value can also have a custom help message associated.
 *
 * This annotation may be used multiple times to allow synonyms.
 */
@Documented
@Repeatable(OptionList.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Option {
    String value();
    String help() default "";
}
