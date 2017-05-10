package cz.cuni.mff.yaclpplib.annotation;

import java.lang.annotation.*;
import cz.cuni.mff.yaclpplib.InvalidOptionValue;

/**
 * Marks the method as a generic argument validator.
 *
 * This method is invoked after all arguments are parsed.
 * If the final state is invalid, throw any runtime exception.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AfterParse {
}
