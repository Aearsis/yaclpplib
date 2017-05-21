package cz.cuni.mff.yaclpplib.annotation;

import java.lang.annotation.*;

/**
 * Marks the method as a generic argument validator. <br/>
 * The marked method must take no arguments. <br/>
 *
 * This method is invoked after all arguments are parsed. <br/>
 * If the final state is invalid, throw any runtime exception or its subclass.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AfterParse {
}
