package cz.cuni.mff.yaclpplib.implementation.drivers;

import cz.cuni.mff.yaclpplib.driver.Driver;
import cz.cuni.mff.yaclpplib.implementation.AmbiguousDriverError;
import cz.cuni.mff.yaclpplib.NoSuchDriverError;

/**
 * Stores available drivers and finds the most specific driver for a given type of an option.
 * Every locator is responsible for its set of drivers.
 */
public interface DriverLocator {
    /**
     * Checks whether this locator has a driver which can convert a string to the given type.
     * @param type demanded type of an option
     * @return true if the locator can provide a driver for the type
     */
    boolean hasDriverFor(Class<?> type);

    /**
     * Provides a driver that can convert a parsed string into given type.
     * @param type demanded type of an option
     * @return a driver creating given type
     * @throws AmbiguousDriverError when the type can be parsed by two different drivers so a driver cannot be chosen
     * @throws NoSuchDriverError when this locator has no driver which can parse the given type
     */
    Driver getDriverFor(Class<?> type) throws AmbiguousDriverError, NoSuchDriverError;
}
