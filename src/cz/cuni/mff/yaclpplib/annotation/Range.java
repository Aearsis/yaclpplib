package cz.cuni.mff.yaclpplib.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Range {
    long minimumValue();
    long maximumValue();
}
