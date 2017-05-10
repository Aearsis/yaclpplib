package cz.cuni.mff.yaclpplib.annotation;

import java.lang.annotation.*;

/**
 * Method annotated with this annotation may receive null as a typed value, with the meaning of missing value.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface OptionalValue {
}
