package cz.cuni.mff.yaclpplib.annotation;

import java.lang.annotation.*;

/**
 * Makes the enum values case-sensitive during parsing.
 *
 * This annotation has a meaning only at enums, it is ignored everywhere else.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface CaseSensitive {

}
