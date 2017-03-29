package cz.cuni.mff.yaclpplib.annotation;

import java.lang.annotation.*;

/**
 * Sets the help message of the option.
 *
 * This message is used for creating an aggregated help message.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface Help {
    String value();
}
