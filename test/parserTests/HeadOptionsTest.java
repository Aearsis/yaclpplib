package parserTests;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.ArgumentParserFactory;
import cz.cuni.mff.yaclpplib.InvalidArgumentsException;
import org.junit.Before;
import org.junit.Test;
import showcase.HeadOptions;

import java.util.List;

import static org.junit.Assert.*;

public class HeadOptionsTest {

    private HeadOptions options;
    private ArgumentParser parser;

    @Before
    public void setUp() {
        options = new HeadOptions();
        parser = ArgumentParserFactory.create();
        parser.addOptions(options);
    }

    @Test
    public void testCombination() throws Exception {
        List<String> plainArguments = parser.requestPlainArguments();
        parser.parse("-n 10 --verbose positional".split(" "));

        assertEquals("Short option -n 10 not parsed correctly.", 10, options.lines);
        assertEquals("Long option --verbose not parsed correctly.", true, options.verbose);
        assertEquals("Positional arguments are broken.", 1, plainArguments.size());
        assertEquals("Positional argument is damaged.", "positional", plainArguments.get(0));
    }

    @Test(expected = InvalidArgumentsException.class)
    public void testAfterParse() throws Exception {
        parser.parse("-n -1".split(" "));
    }

    @Test
    public void testLongBooleanOptions() throws Exception {
        parser.parse("--silent=true --verbose".split(" "));

        assertEquals("Long option --silent=true not parsed correctly.", true, options.quiet);
        assertEquals("Long option --verbose not parsed correctly.", true, options.verbose);
    }

    @Test
    public void testLongIntegerOptions() throws Exception {
        parser.parse("--lines=42".split(" "));

        assertEquals("Long option --lines=42 not parsed correctly.", 42, options.lines);
    }

}