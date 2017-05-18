package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class StringDriverTest {
    private StringDriver instance;

    @Before
    public void setUp() throws Exception {
        instance = new StringDriver();
    }

    @Test
    public void integration() throws Exception {
        DriverIntegration.test(instance, new JustValue("0"));
    }

    @Test
    public void parse() throws Exception {
        testParse("any string", "any string");
        testFailing(null);
    }

    private void testFailing(String input) {
        try {
            instance.parse(new JustValue(input));
            fail();
        } catch (InvalidOptionValue expected) {}
    }

    private void testParse(String input, String expected) throws InvalidOptionValue {
        assertEquals(expected, instance.parse(new JustValue(input)));
    }

    @Test
    public void getReturnType() throws Exception {
        assertEquals(instance.getReturnType(), String.class);
    }

}