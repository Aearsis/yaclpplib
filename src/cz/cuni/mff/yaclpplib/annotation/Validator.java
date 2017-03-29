package cz.cuni.mff.yaclpplib.annotation;

import java.lang.annotation.*;
import cz.cuni.mff.yaclpplib.InvalidOptionValue;

/**
 * Marks the method as a generic argument validator.
 *
 * This method is invoked after all arguments are parsed.
 * If the annotated method returns false, parsing failed and an {@link InvalidOptionValue} is thrown to user.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Validator {
}
