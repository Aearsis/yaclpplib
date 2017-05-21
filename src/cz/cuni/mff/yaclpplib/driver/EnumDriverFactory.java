package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.implementation.AmbiguousDriverError;
import cz.cuni.mff.yaclpplib.implementation.drivers.DriverLocator;
import cz.cuni.mff.yaclpplib.NoSuchDriverError;

/**
 * Creates {@link GenericEnumDriver} for enums.
 */
public class EnumDriverFactory implements DriverLocator {
    @Override
    public boolean hasDriverFor(Class<?> type) {
        return type.isEnum();
    }

    @Override
    public Driver getDriverFor(Class<?> type) throws AmbiguousDriverError, NoSuchDriverError {
        return new GenericEnumDriver(type);
    }
}
