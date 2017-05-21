package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.InvalidSetupError;
import cz.cuni.mff.yaclpplib.annotation.CaseSensitive;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnumDriverFactoryTest {

    private EnumDriverFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new EnumDriverFactory();
    }

    private enum insensitiveEnum {
        InSeNsItIvE, sEnSiTiVe
    }

    @CaseSensitive
    private enum sensitiveEnum {
       sensitive, SENSITIVE
    }

    private enum invalidEnum {
        sensitive, SENSITIVE
    }

    @Test
    public void testInsensitive() throws Exception {
        Driver driver = factory.getDriverFor(insensitiveEnum.class);

        assertEquals(insensitiveEnum.InSeNsItIvE, driver.parse(new JustValue("insensitive")));
        assertEquals(insensitiveEnum.sEnSiTiVe, driver.parse(new JustValue("sensitive")));
    }

    @Test
    public void testSensitive() throws Exception {
        Driver driver = factory.getDriverFor(sensitiveEnum.class);

        assertEquals(sensitiveEnum.SENSITIVE, driver.parse(new JustValue("SENSITIVE")));
        assertEquals(sensitiveEnum.sensitive, driver.parse(new JustValue("sensitive")));
    }

    @Test(expected = InvalidOptionValue.class)
    public void testSensitiveNotFound() throws Exception {
        Driver driver = factory.getDriverFor(sensitiveEnum.class);

        driver.parse(new JustValue("Sensitive"));
    }

    @Test(expected = InvalidSetupError.class)
    public void testWrongSetup() throws Exception {
        factory.getDriverFor(invalidEnum.class);
    }
}
