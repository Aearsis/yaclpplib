package cz.cuni.mff.yaclpplib.annotation;

import java.lang.annotation.*;

/**
 * Dummy annotation to allow using annotation {@link Option} more than once.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface OptionList {
    Option[] value();
}
