package cz.cuni.mff.yaclpplib.annotation;

import java.lang.annotation.*;
import cz.cuni.mff.yaclpplib.IllegalOptionValue;

/**
 * <p>Sets a short or long option text to go with this attribute
 * or method. Each value can also have a custom help message associated. </p>
 *
 * <p>This annotation may be used multiple times to allow synonyms. </p>
 *
 * <p>You can annotate fields and methods.
 * Fields can be either of simple types (int, String, bool etc.), enums or arrays of these. </p>
 *
 * <p>Methods can have all from above.
 * These methods can validate the argument and reject it with {@link IllegalOptionValue} exception. </p>
 *
 * <p>The order of setting fields / calling methods is not defined.</p>
 */
@Documented
@Repeatable(OptionList.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Option {
    /**
     * The option name, such as -o or --option.
     */
    String value();

    /**
     * The name of the option that appears in help.
     * It can be different from the actual option if needed.
     */
    String help() default "";
}
