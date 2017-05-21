package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.driver.Driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A driver storage that caches query results.
 */
public class DriverCache implements DriverLocator {

    final Map<Class<?>, Driver> cache = new HashMap<>();
    final List<DriverLocator> locators = new ArrayList<>();

    public void addDriverLocator(DriverLocator locator) {
        locators.add(locator);
    }

    @Override
    public boolean hasDriverFor(Class<?> type) {
        return locators.stream().anyMatch((l) -> l.hasDriverFor(type));
    }

    @Override
    public Driver getDriverFor(Class<?> type) throws AmbiguousDriverError, NoSuchDriverError {
        return cache
                .computeIfAbsent(type, (x) -> locators.stream()
                    .filter((l) -> l.hasDriverFor(type))
                    .findFirst()
                    .map((l) -> l.getDriverFor(type))
                    .orElseThrow(NoSuchDriverError::new));
    }
}
