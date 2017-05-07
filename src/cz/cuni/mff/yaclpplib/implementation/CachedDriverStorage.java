package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.DuplicateDriverError;
import cz.cuni.mff.yaclpplib.driver.Driver;

import java.util.HashMap;
import java.util.Map;

/**
 * A driver storage that caches query results.
 */
public class CachedDriverStorage implements DriverStorage {

    final Map<Class<?>, Driver<?>> cache = new HashMap<>();
    final DriverStorage decoratedStorage;

    public CachedDriverStorage(DriverStorage decoratedStorage) {
        this.decoratedStorage = decoratedStorage;
    }

    @Override
    public <T> void add(final Driver<T> driver) throws DuplicateDriverError {
        decoratedStorage.add(driver);
        cache.clear();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Driver<T> find(final Class<? super T> type) throws AmbiguousDriverError, NoSuchDriverError {
        return (Driver<T>) cache.computeIfAbsent(type, (x) -> decoratedStorage.find(type));
    }
}
