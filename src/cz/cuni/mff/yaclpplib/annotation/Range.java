package cz.cuni.mff.yaclpplib.annotation;

import java.lang.annotation.*;
import cz.cuni.mff.yaclpplib.*;

/**
 * <p>Annotation used at integral options. </p>
 *
 * <p>This is a subject for different library, field validator. It is here only to satisfy the assignment requirements. </p>
 *
 * <p>When parsing the annotated option, if the value doesn't fit in the given range,
 * {@link ArgumentParser} will throw {@link InvalidOptionValue} exception.</p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Range {
    /**
     * Minimal allowed value.
     */
    long minimumValue();

    /**
     * Maximal allowed value.
     */
    long maximumValue();
}
