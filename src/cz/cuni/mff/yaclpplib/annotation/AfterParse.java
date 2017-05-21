package cz.cuni.mff.yaclpplib.annotation;

import java.lang.annotation.*;

/**
 * <p>Marks the method as a generic argument validator.
 * The marked method must take no arguments.</p>
 *
 * <p>This method is invoked after all arguments are parsed.
 * If the final state is invalid, throw any runtime exception or its subclass. </p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AfterParse {
}
