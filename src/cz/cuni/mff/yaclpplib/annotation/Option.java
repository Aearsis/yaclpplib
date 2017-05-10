package cz.cuni.mff.yaclpplib.annotation;

import java.lang.annotation.*;

/**
 * Sets a short or long option text to go with this attribute
 * or method. Each value can also have a custom help message associated.
 *
 * This annotation may be used multiple times to allow synonyms.
 *
 * You can annotate fields and methods.
 * Fields can be either of simple types (int, String, bool etc.), enums or arrays of these.
 *
 * Methods can have all from above and Optional&lt;T&gt;, with T being any field available type.
 * These method can validate the argument and reject it with IllegalOptionValue exception.
 *
 * The order of setting fields / calling methods is not defined.
 */
@Documented
@Repeatable(OptionList.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Option {
    String value();
    String help() default "";
}
