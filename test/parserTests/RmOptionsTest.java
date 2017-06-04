package parserTests;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.ArgumentParserFactory;
import org.junit.Before;
import org.junit.Test;
import showcase.RmOptions;

import static org.junit.Assert.*;

import java.util.List;

public class RmOptionsTest {
    ArgumentParser parser;
    List<String> unhandledArgs;
    RmOptions options;

    @Before
    public void setUp() {
        options = new RmOptions();
        parser = ArgumentParserFactory.create();
        parser.addOptions(options);
        unhandledArgs = parser.requestPositionalArguments();
    }

    @Test
    public void testEnumShort() throws Exception {
        parser.parse("-I".split(" "));

        assertEquals("Prompt should be set to once.", RmOptions.Prompt.ONCE, options.prompt);
    }

    @Test
    public void testEnumLongNoValue() throws Exception {
        parser.parse("--interactive".split(" "));

        assertEquals("Prompt should be set to always.", RmOptions.Prompt.ALWAYS, options.prompt);
    }

    @Test
    public void testEnumLongValue() throws Exception {
        parser.parse("--interactive=NEVER".split(" "));

        assertEquals("Prompt should be set to never.", RmOptions.Prompt.NEVER, options.prompt);
    }

    @Test
    public void testPrivateField() throws Exception {
        parser.parse("--one-file-system".split(" "));

        assertEquals("One file system should be true now.", true, options.isOneFileSystem());
    }

    @Test
    public void testMultiple() throws Exception {
        parser.parse("-f -R --dir unexpected --interactive=ONCE --verbose=false unexpected".split(" "));

        assertEquals("Force should be set to true", true, options.force);
        assertEquals("Recursive should be set to true", true, options.recursive);
        assertEquals("Remove directories should be set to true", true, options.removeEmptyDirs);
        assertEquals("Interactive should be set to once", RmOptions.Prompt.ONCE, options.prompt);
        assertEquals("There should be 2 positional arguments.", 2, unhandledArgs.size());
        assertEquals("First positional argument is damaged.", "unexpected", unhandledArgs.get(0));
        assertEquals("Second positional argument is damaged.", "unexpected", unhandledArgs.get(1));
    }

}
