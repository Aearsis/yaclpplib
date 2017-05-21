package cz.cuni.mff.yaclpplib.implementation.drivers;

import cz.cuni.mff.yaclpplib.driver.Driver;
import cz.cuni.mff.yaclpplib.implementation.AmbiguousDriverError;
import cz.cuni.mff.yaclpplib.NoSuchDriverError;

/**
 * Stores available drivers and finds the most specific driver for a type of option.
 */
public interface DriverLocator {
    boolean hasDriverFor(Class<?> type);
    Driver getDriverFor(Class<?> type) throws AmbiguousDriverError, NoSuchDriverError;
}
