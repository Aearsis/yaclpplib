package cz.cuni.mff.yaclpplib.implementation.drivers;

import cz.cuni.mff.yaclpplib.driver.Driver;
import cz.cuni.mff.yaclpplib.driver.GenericCaseInsensitiveEnumDriver;
import cz.cuni.mff.yaclpplib.driver.GenericEnumDriver;
import cz.cuni.mff.yaclpplib.annotation.CaseSensitive;
import cz.cuni.mff.yaclpplib.implementation.AmbiguousDriverError;
import cz.cuni.mff.yaclpplib.NoSuchDriverError;

/**
 * Creates {@link GenericEnumDriver} and {@link GenericCaseInsensitiveEnumDriver} for enums.
 */
public class EnumDriverFactory implements DriverLocator {
    @Override
    public boolean hasDriverFor(Class<?> type) {
        return type.isEnum();
    }

    @Override
    public Driver getDriverFor(Class<?> type) throws AmbiguousDriverError, NoSuchDriverError {
        if (type.getDeclaredAnnotation(CaseSensitive.class) == null) {
            return new GenericCaseInsensitiveEnumDriver(type);
        }
        else {
            return new GenericEnumDriver(type);
        }
    }
}
