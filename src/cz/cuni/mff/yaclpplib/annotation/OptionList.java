package cz.cuni.mff.yaclpplib.annotation;

import cz.cuni.mff.yaclpplib.annotation.Option;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface OptionList {
    Option[] value();
}
