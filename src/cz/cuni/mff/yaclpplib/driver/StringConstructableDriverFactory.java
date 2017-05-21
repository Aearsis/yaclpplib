package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.implementation.AmbiguousDriverError;
import cz.cuni.mff.yaclpplib.implementation.drivers.DriverLocator;
import cz.cuni.mff.yaclpplib.NoSuchDriverError;

/**
 * Creates {@link GenericStringConstructableDriver} for given types.
 */
public class StringConstructableDriverFactory implements DriverLocator {
    @Override
    public boolean hasDriverFor(Class<?> type) {
        return GenericStringConstructableDriver.hasStringConstructor(type);
    }

    @Override
    public Driver getDriverFor(Class<?> type) throws AmbiguousDriverError, NoSuchDriverError {
        return new GenericStringConstructableDriver(type);
    }
}
