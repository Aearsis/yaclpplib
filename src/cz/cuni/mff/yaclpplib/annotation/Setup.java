package cz.cuni.mff.yaclpplib.annotation;

import java.lang.annotation.*;

/**
 * This method will be called by the ArgumentParser after attaching.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Setup {
}
