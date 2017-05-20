package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.ArgumentParserFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ArgumentParserImplTest {

    private ArgumentParserImpl parser;

    @Before
    public void setUp() throws Exception {
        parser = (ArgumentParserImpl) ArgumentParserFactory.create();
    }

    @Test
    public void unexpectedParameterHandler() throws Exception {
        final String[] out = new String[1];
        String in = "param";

        parser.setUnexpectedParameterHandler((parameter) -> out[0] = parameter);

        parser.parse(new String[] { in });

        assertEquals("Unexpected parameter handler was not called.", in, out[0]);
    }

    @Test
    public void justPositionalArguments() throws Exception {
        String[] args = new String[] { "--donothing", "-f", "really", "do", "nothing"};

        List<String> plainArguments = parser.requestPlainArguments();
        parser.parse(args);

        assertEquals("Plain arguments do not have correct size.", plainArguments.size(), args.length);
        for (int i = 0; i < args.length; i++)
            assertEquals("Plain argument " + i + " was not parsed correctly.", plainArguments.get(i), args[i]);
    }

    @Test
    public void testHelp() throws Exception {
        String help = parser.getHelp();

        assertEquals("Help text should contain the section title.", true, help.contains("Default options"));
        assertEquals("Help text should contain '--help'.", true, help.contains("--help"));
        assertEquals("Help text should contain 'Print a usage'.", true, help.contains("Print a usage"));
    }
}