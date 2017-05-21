package cz.cuni.mff.yaclpplib.annotation;

import java.lang.annotation.*;

/**
 * <p>Makes the enum values case-sensitive during parsing. </p>
 *
 * <p>This annotation has a meaning only at enums, it is ignored everywhere else.</p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface CaseSensitive {

}
