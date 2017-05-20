package cz.cuni.mff.yaclpplib.driver;

import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BooleanDriverTest {

    private BooleanDriver instance;

    @Before
    public void setUp() throws Exception {
        instance = new BooleanDriver();
    }

    @Test
    public void integration() throws Exception {
        DriverIntegration.test(instance, new JustValue("false"));
    }

    @Test
    public void parse() throws Exception {
        testParse("enabled", true);
        testParse("enable", true);
        testParse("true", true);
        testParse("yes", true);
        testParse("on", true);
        testParse("t", true);
        testParse("y", true);
        testParse("1", true);

        testParse("disabled", false);
        testParse("disable", false);
        testParse("false", false);
        testParse("no", false);
        testParse("off", false);
        testParse("f", false);
        testParse("n", false);
        testParse("0", false);

        testFailing("maybe");
        testFailing("");
    }

    private void testFailing(String input) {
        try {
            instance.parse(new JustValue(input));
            fail();
        } catch (InvalidOptionValue expected) {}
    }

    private void testParse(String input, boolean expected) throws InvalidOptionValue {
        assertEquals(expected, instance.parse(new JustValue(input)));
    }

    @Test
    public void getReturnType() throws Exception {
        assertEquals(instance.getReturnType(), Boolean.class);
    }

}