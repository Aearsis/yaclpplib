package cz.cuni.mff.yaclpplib.annotation;

import java.lang.annotation.*;

/**
 * Method annotated with this annotation may receive null as a typed value, with the meaning of missing value.
 * This annotation cannot be used with methods having primitive types as parameter, as those cannot
 * be set to null. Use wrapper types instead.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface OptionalValue {
}
