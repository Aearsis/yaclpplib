package cz.cuni.mff.yaclpplib.implementation;

import org.junit.Test;

import static org.junit.Assert.*;

public class ShortOptionValueTest {

    @Test
    public void testJustName() throws Exception {
        ShortOptionValue value = new ShortOptionValue("-a");

        assertEquals("-a", value.getName());
    }

    @Test
    public void testNameWithValue() throws Exception {
        ShortOptionValue value = new ShortOptionValue("-k");

        value.completeValue(new TokenList(new String[] { "-n" }), ValuePolicy.MANDATORY);

        assertEquals("-k", value.getName());
        assertEquals(true, value.hasValue());
        assertEquals("-n", value.getValue());
    }

    @Test
    public void testShortFormat() throws Exception {
        ShortOptionValue value = new ShortOptionValue("-kvalue");

        value.completeValue(new TokenList(new String[] { "nextvalue" }), ValuePolicy.MANDATORY);

        assertEquals("-k", value.getName());
        assertEquals(true, value.hasValue());
        assertEquals("value", value.getValue());
    }
}