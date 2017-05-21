package cz.cuni.mff.yaclpplib.implementation.drivers;

import cz.cuni.mff.yaclpplib.DuplicateDriverError;
import cz.cuni.mff.yaclpplib.driver.Driver;

/**
 * A storage for various drivers.
 */
interface DriverStorage {
    /**
     * Adds a new driver to the storage.
     * @param driver added driver
     * @throws DuplicateDriverError when there is already a driver for the given type
     */
    void add(Driver driver) throws DuplicateDriverError;
}
