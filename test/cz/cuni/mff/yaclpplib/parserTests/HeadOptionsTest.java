package cz.cuni.mff.yaclpplib.parserTests;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.ArgumentParserFactory;
import org.junit.Test;
import showcase.HeadOptions;

import java.util.List;

import static org.junit.Assert.*;

public class HeadOptionsTest {

    @Test
    public void testCombination() throws Exception {
        HeadOptions options = new HeadOptions();
        ArgumentParser parser = ArgumentParserFactory.create();

        List<String> plainArguments = parser.requestPlainArguments();
        parser.parse("-n 10 --verbose positional".split(" "));

        assertEquals("Short option -n 10 not parsed correctly.", 10, options.lines);
        assertEquals("Long option --verbose not parsed correctly.", true, options.verbose);
        assertEquals("Positional arguments are broken.", 1, plainArguments.size());
        assertEquals("Positional argument is damaged.", "positional", plainArguments.get(0));
    }

    @Test
    public void testLongBooleanOptions() throws Exception {
        HeadOptions options = new HeadOptions();
        ArgumentParser parser = ArgumentParserFactory.create();

        parser.parse("--silent=true --verbose true".split(" "));

        assertEquals("Long option --silent=false not parsed correctly.", true, options.quiet);
        assertEquals("Long option --verbose false not parsed correctly.", true, options.verbose);
    }

    @Test
    public void testLongIntegerOptions() throws Exception {
        HeadOptions options = new HeadOptions();
        ArgumentParser parser = ArgumentParserFactory.create();

        parser.parse("--lines=42 --bytes 42".split(" "));

        assertEquals("Long option --lines=42 not parsed correctly.", 42, options.lines);
        assertEquals("Long option --bytes 42 not parsed correctly.", 42, options.bytes);
    }
}