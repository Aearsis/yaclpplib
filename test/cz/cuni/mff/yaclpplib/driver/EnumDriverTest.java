package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class EnumDriverTest {

    enum SimpleEnum {
        A, B, C
    }

    @Test
    public void getReturnType() throws Exception {
        GenericEnumDriver driver = new GenericEnumDriver(SimpleEnum.class);
        assertEquals(SimpleEnum.class, driver.getReturnType());
    }

    @Test
    public void testSimpleEnum() throws Exception {
        GenericEnumDriver driver = new GenericEnumDriver(SimpleEnum.class);

        for (SimpleEnum val : SimpleEnum.values()) {
            JustValue a = new JustValue(val.toString());
            assertEquals(val, driver.parse(a));
        }
    }

    @Test(expected = InvalidOptionValue.class)
    public void testInvalid() throws Exception {
        GenericEnumDriver driver = new GenericEnumDriver(SimpleEnum.class);

        driver.parse(new JustValue("Invalid"));
    }

}