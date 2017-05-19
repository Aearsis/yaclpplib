package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.DuplicateDriverError;
import cz.cuni.mff.yaclpplib.driver.Driver;

/**
 * Stores available drivers and finds the most specific driver for a type of option.
 */
public interface DriverLocator {
    Driver getDriverFor(Class<?> type) throws AmbiguousDriverError, NoSuchDriverError;
}
