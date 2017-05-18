package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IntegerDriverTest {
    private IntegerDriver instance;

    @Before
    public void setUp() throws Exception {
        instance = new IntegerDriver();
    }

    @Test
    public void integration() throws Exception {
        DriverIntegration.test(instance, new JustValue("0"));
    }

    @Test
    public void parse() throws Exception {
        testParse("1", 1);
        testParse("42", 42);
        testParse("-100", -100);
        testParse("+1000", 1000);

        testFailing("zero");
        testFailing("");
        testFailing(null);
    }

    private void testFailing(String input) {
        try {
            instance.parse(new JustValue(input));
            fail();
        } catch (InvalidOptionValue expected) {}
    }

    private void testParse(String input, int expected) throws InvalidOptionValue {
        assertEquals(expected, instance.parse(new JustValue(input)).intValue());
    }

    @Test
    public void getReturnType() throws Exception {
        assertEquals(instance.getReturnType(), Integer.class);
    }

}