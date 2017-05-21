package parserTests;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.ArgumentParserFactory;
import cz.cuni.mff.yaclpplib.Options;
import cz.cuni.mff.yaclpplib.annotation.Option;
import cz.cuni.mff.yaclpplib.implementation.ArgumentParserImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TypesTest {

    private ArgumentParser parser;
    private TestOptions instance;

    private static class TestOptions implements Options {
        @Option("--int")
        int i;

        @Option("--short")
        short s;

        @Option("--long")
        long l;

        @Option("--char")
        char c;

        @Option("--string")
        String str;

        @Option("--byte")
        byte b;

        @Option("--float")
        float f;

        @Option("--double")
        double d;

        @Option("--boolean")
        boolean bool;
    }

    @Before
    public void setUp() throws Exception {
        parser = ArgumentParserFactory.create();
        instance = new TestOptions();
        parser.addOptions(instance);
    }

    @Test
    public void testInteger() throws Exception {
        parser.parse(new String[] { "--int=42" });
        assertEquals(42, instance.i);
    }

    @Test
    public void testLong() throws Exception {
        parser.parse(new String[] { "--long=" + Long.MAX_VALUE });
        assertEquals(Long.MAX_VALUE, instance.l);
    }

    @Test
    public void testShort() throws Exception {
        parser.parse(new String[] { "--short=42" });
        assertEquals(42, instance.s);
    }

    @Test
    public void testByte() throws Exception {
        parser.parse(new String[] { "--byte=42" });
        assertEquals(42, instance.b);
    }

    @Test
    public void testFloat() throws Exception {
        parser.parse(new String[] { "--float=1" });
        assertEquals(1, instance.f, 0);
    }

    @Test
    public void testDouble() throws Exception {
        parser.parse(new String[] { "--double=1" });
        assertEquals(1, instance.d, 0);
    }

    @Test
    public void testBoolean() throws Exception {
        parser.parse(new String[] { "--boolean=true" });
        assertEquals(true, instance.bool);
    }

    @Test
    public void testCharacter() throws Exception {
        parser.parse(new String[] { "--char=x" });
        assertEquals('x', instance.c);
    }

    @Test
    public void testString() throws Exception {
        parser.parse(new String[] { "--string=ihavelostthegame" });
        assertEquals("ihavelostthegame", instance.str);
    }
}
