package cz.cuni.mff.yaclpplib.annotation;

import java.lang.annotation.*;

/**
 * <p>Sets the help message of the option. </p>
 *
 * <p>This message is used for creating an aggregated help message.</p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface Help {
    String value();
}
