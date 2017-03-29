package cz.cuni.mff.yaclpplib.annotation;

import java.lang.annotation.*;
import cz.cuni.mff.yaclpplib.ArgumentParser;

/**
 * Makes the option mandatory. If the option has this annotation
 * and it is not present in program arguments, {@link ArgumentParser} will throw an exception.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Mandatory {

}
