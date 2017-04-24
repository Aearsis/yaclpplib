package cz.cuni.mff.yaclpplib.driver;


import cz.cuni.mff.yaclpplib.OptionValue;

import static org.junit.Assert.assertEquals;

public class DriverIntegration {

    public static void test(Driver driver, OptionValue validOptionValue) throws Exception {
        assertEquals(driver.getReturnType(), driver.parse(validOptionValue).getClass());
    }

}