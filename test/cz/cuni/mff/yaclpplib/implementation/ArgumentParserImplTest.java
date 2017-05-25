package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.ArgumentParserFactory;
import cz.cuni.mff.yaclpplib.InvalidSetupError;
import cz.cuni.mff.yaclpplib.Options;
import cz.cuni.mff.yaclpplib.annotation.AfterParse;
import cz.cuni.mff.yaclpplib.annotation.BeforeParse;
import cz.cuni.mff.yaclpplib.annotation.Option;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.junit.Assert.*;

public class ArgumentParserImplTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

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

    @Test(expected = InvalidSetupError.class)
    public void testMultipleOptions() throws Exception {
        parser.addOptions(new Options() {
            @Option("--verbose")
            boolean verbose;

            @Option("--verbose")
            void method() {}
        });
    }

    @Test
    public void testBeforeParseWithoutArg() throws Exception {
        final String message = "hander called";
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(message);

        parser.addOptions(new Options() {
            @BeforeParse
            void foo() {
                throw new RuntimeException(message);
            }
        });

        parser.parse(new String[] {});
    }

    @Test
    public void testBeforeParseWithArg() throws Exception {
        final String message = "hander called";
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(message);

        parser.addOptions(new Options() {
            @BeforeParse
            void foo(ArgumentParser parser) {
                throw new RuntimeException(message);
            }
        });

        parser.parse(new String[] {});
    }

    @Test
    public void testAfterParseWithoutArg() throws Exception {
        final String message = "hander called";
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(message);

        parser.addOptions(new Options() {
            @AfterParse
            void foo() {
                throw new RuntimeException(message);
            }
        });

        parser.parse(new String[] {});
    }

    @Test
    public void testAfterParseWithArg() throws Exception {
        final String message = "hander called";
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(message);

        parser.addOptions(new Options() {
            @AfterParse
            void foo(ArgumentParser parser) {
                throw new RuntimeException(message);
            }
        });

        parser.parse(new String[] {});
    }

    @Test
    public void testComposition() throws Exception {
        final String message = "hander called";
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(message);

        parser.addOptions(new Options() {
            Options composited = new Options() {
                @BeforeParse
                void foo() {
                    throw new RuntimeException(message);
                }
            };
        });

        parser.parse(new String[] {});
    }
}