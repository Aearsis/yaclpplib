package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.InvalidSetupError;
import cz.cuni.mff.yaclpplib.driver.Driver;

import java.util.Collection;

/**
 * There are two or more drivers that can handle this type of option.
 */
public class AmbiguousDriverError extends InvalidSetupError {

    private final Collection<Driver> availableDrivers;
    private final Class<?> forType;

    public AmbiguousDriverError(Class<?> forType, Collection<Driver> availableDrivers) {
        this.forType = forType;
        this.availableDrivers = availableDrivers;
    }

    @Override
    public String getMessage() {
        return String.format("Cannot choose a driver for %s - more uncomparable drivers exists: %s",
                forType.getTypeName(),
                String.join(",", availableDrivers.parallelStream().map((driver)
                        -> driver.getClass().getTypeName()).toArray(String[]::new)));
    }
}
