package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.DuplicateDriverError;
import cz.cuni.mff.yaclpplib.driver.Driver;

import java.util.HashMap;
import java.util.Map;

/**
 * A driver storage that caches query results.
 */
public class DriverCache<T extends DriverStorage & DriverLocator> implements DriverLocator, DriverStorage {

    final Map<Class<?>, Driver> cache = new HashMap<>();
    final T decoratedStorage;

    public DriverCache(T decoratedStorage) {
        this.decoratedStorage = decoratedStorage;
    }

    @Override
    public void add(Driver driver) throws DuplicateDriverError {
        decoratedStorage.add(driver);
        cache.clear();
    }

    @Override
    public Driver getDriverFor(Class<?> type) throws AmbiguousDriverError, NoSuchDriverError {
        return cache.computeIfAbsent(type, (x) -> decoratedStorage.getDriverFor(type));
    }
}
