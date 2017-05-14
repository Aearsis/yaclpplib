package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.DuplicateDriverError;
import cz.cuni.mff.yaclpplib.driver.Driver;

/**
 * Stores available drivers and finds the most specific driver for a type of option.
 */
public interface DriverStorage {
    <T> void add(Driver<T> driver) throws DuplicateDriverError;
    <T> Driver<T> find(Class<? super T> type) throws AmbiguousDriverError, NoSuchDriverError;
    <T> boolean contains(Class<? super T> type);
}
