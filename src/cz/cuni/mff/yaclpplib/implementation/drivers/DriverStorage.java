package cz.cuni.mff.yaclpplib.implementation.drivers;

import cz.cuni.mff.yaclpplib.DuplicateDriverError;
import cz.cuni.mff.yaclpplib.driver.Driver;

/**
 * Capable of storing drivers.
 */
interface DriverStorage {
    void add(Driver driver) throws DuplicateDriverError;
}
