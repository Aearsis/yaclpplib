package cz.cuni.mff.yaclpplib;

/**
 * The newly added driver handles the same type of option as one already added.
 */
public class DuplicateDriverError extends InvalidSetupError {

    final Class<?> collidingClass;

    public DuplicateDriverError(Class<?> collidingClass) {
        this.collidingClass = collidingClass;
    }

    @Override
    public String getMessage() {
        return "A driver for " + collidingClass.getTypeName() + " already exists.";
    }
}
