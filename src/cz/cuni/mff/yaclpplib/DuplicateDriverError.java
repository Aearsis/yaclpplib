package cz.cuni.mff.yaclpplib;

/**
 * The newly added driver handles the same type of option as one already added.
 */
public class DuplicateDriverError extends InvalidSetupError {

    private final Class<?> collidingClass;

    public DuplicateDriverError(Class<?> collidingClass) {
        this.collidingClass = collidingClass;
    }

    @Override
    public String getMessage() {
        return "A driver for " + collidingClass.getTypeName() + " already exists.";
    }

    /**
     * Return the Class instance of the colliding class which cause this exception.
     * @return the Class instance of colliding class
     */
    public Class<?> getCollidingClass() {
        return collidingClass;
    }
}
