package cz.cuni.mff.yaclpplib.annotation;

import java.lang.annotation.*;
import cz.cuni.mff.yaclpplib.*;

/**
 * Annotation used at integral options.
 *
 * When parsing the annotated option, if the value doesn't fit in the given range,
 * {@link ArgumentParser} will throw {@link InvalidOptionValue} exception.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Range {
    long minimumValue();
    long maximumValue();
}
