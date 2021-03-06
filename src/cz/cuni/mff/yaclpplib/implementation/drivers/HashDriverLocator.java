package cz.cuni.mff.yaclpplib.implementation.drivers;

import cz.cuni.mff.yaclpplib.driver.Driver;
import cz.cuni.mff.yaclpplib.DuplicateDriverError;
import cz.cuni.mff.yaclpplib.implementation.AmbiguousDriverError;
import cz.cuni.mff.yaclpplib.NoSuchDriverError;

import java.util.*;

/**
 * Default DriverLocator and storage implementation using a HashMap mapped by return types.
 */
public class HashDriverLocator implements DriverLocator, DriverStorage {

    private final Map<Class<?>, Driver> drivers = new HashMap<>();

    @Override
    public void add(Driver driver) throws DuplicateDriverError {
        final Class<?> returnType = driver.getReturnType();

        if (drivers.containsKey(returnType)) {
            throw new DuplicateDriverError(returnType);
        }

        drivers.put(returnType, driver);
    }

    /**
     * Finds the most suitable driver for type. That is the least specific one creating a type assignable to type.
     * @param type a type of option value
     * @return driver that can produce the type
     */
    @Override
    @SuppressWarnings("unchecked")
    public Driver getDriverFor(Class<?> type) throws AmbiguousDriverError, NoSuchDriverError {
        final Set<Driver> bestDrivers = new HashSet<>();

        outerLoop:
        for (Driver currentDriver : drivers.values()) {
            if (!type.isAssignableFrom(currentDriver.getReturnType())) {
                continue;
            }

            // Skip current if there is a less specialized driver
            for (Driver<?> lessSpecific : bestDrivers) {
                if (lessSpecific.getReturnType().isAssignableFrom(currentDriver.getReturnType())) {
                    continue outerLoop;
                }
            }

            // Remove all more specialized drivers
            bestDrivers.removeIf((Driver moreSpecific)
                    -> currentDriver.getReturnType().isAssignableFrom(moreSpecific.getReturnType()));

            bestDrivers.add(currentDriver);
        }

        switch (bestDrivers.size()) {
            case 0:
                throw new NoSuchDriverError();
            case 1:
                return bestDrivers.toArray(new Driver[1])[0];
            default:
                throw new AmbiguousDriverError(type, bestDrivers);
        }
    }

    /**
     * Checks if the driver storage contains a driver with given return type.
     * @param type desired return type
     * @return true if there exists a driver with such return type
     */
    @Override
    public boolean hasDriverFor(Class <?> type) {
        return drivers.values().stream().anyMatch((driver) -> type.isAssignableFrom(driver.getReturnType()));
    }

}
