package parserTests;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.ArgumentParserFactory;
import cz.cuni.mff.yaclpplib.Options;
import cz.cuni.mff.yaclpplib.annotation.Option;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * These tests represent "a common sense", that flags are represented by boolean options.
 */
public class BooleanFlagsTest {

    class FlagsTest implements Options {
        @Option("-s")
        @Option("--long")
        boolean flag = false;
    }

    private ArgumentParser parser;
    private FlagsTest instance;

    @Before
    public void setUp() throws Exception {
        parser = ArgumentParserFactory.create();
        instance = parser.addOptions(new FlagsTest());
    }

    @Test
    public void testShortFlag() throws Exception {
        parser.parse("-s".split(" "));
        assertEquals(true, instance.flag);
    }

    @Test
    public void testLongFlag() throws Exception {
        parser.parse("--long".split(" "));
        assertEquals(true, instance.flag);
    }

    @Test
    public void testShortFalse() throws Exception {
        instance.flag = true;
        parser.parse("-sfalse".split(" "));
        assertEquals(false, instance.flag);
    }

    @Test
    public void testLongFalse() throws Exception {
        instance.flag = true;
        parser.parse("--long=false".split(" "));
        assertEquals(false, instance.flag);
    }

    @Test
    public void testShortPositional() throws Exception {
        List<String> plainArguments = parser.requestPlainArguments();
        parser.parse("-s false".split(" "));
        assertEquals(true, instance.flag);
        assertEquals(1, plainArguments.size());
    }

    @Test
    public void testLongPositional() throws Exception {
        List<String> plainArguments = parser.requestPlainArguments();
        parser.parse("--long false".split(" "));
        assertEquals(true, instance.flag);
        assertEquals(1, plainArguments.size());
    }
}
