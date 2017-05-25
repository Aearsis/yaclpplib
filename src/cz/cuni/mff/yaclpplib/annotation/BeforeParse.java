package cz.cuni.mff.yaclpplib.annotation;

import java.lang.annotation.*;

/**
 * This method is invoked before any argument is parsed.
 * You can use it as an initializer before parsing.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface BeforeParse {
}
