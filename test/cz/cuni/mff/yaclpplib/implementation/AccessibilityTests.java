package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.ArgumentParserFactory;
import cz.cuni.mff.yaclpplib.Options;
import cz.cuni.mff.yaclpplib.annotation.AfterParse;
import cz.cuni.mff.yaclpplib.annotation.Option;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AccessibilityTests {

    private ArgumentParser parser;

    @Before
    public void setUp() throws Exception {
        parser = ArgumentParserFactory.create();
    }

    @Test
    public void testPrivateField() throws Exception {
        parser.addOptions(new Options() {
            @Option("--option")
            private int field;
        });

        parser.parse("--option=10".split(" "));
    }

    @Test
    public void testProtectedField() throws Exception {
        parser.addOptions(new Options() {
            @Option("--option")
            protected int field;
        });

        parser.parse("--option=10".split(" "));
    }

    @Test
    public void testPublicField() throws Exception {
        parser.addOptions(new Options() {
            @Option("--option")
            public int field;
        });

        parser.parse("--option=10".split(" "));
    }

    @Test
    public void testPrivateMethod() throws Exception {
        parser.addOptions(new Options() {
            @Option("--option")
            private void option(int arg) {}
        });

        parser.parse("--option=10".split(" "));
    }

    @Test
    public void testProtectedMethod() throws Exception {
        parser.addOptions(new Options() {
            @Option("--option")
            protected void option(int arg) {}
        });

        parser.parse("--option=10".split(" "));
    }

    @Test
    public void testPublicMethod() throws Exception {
        parser.addOptions(new Options() {
            @Option("--option")
            public void option(int arg) {}
        });

        parser.parse("--option=10".split(" "));
    }

    @Test(expected = RuntimeException.class)
    public void testPrivateAfterParse() throws Exception {
        parser.addOptions(new Options() {
            @AfterParse
            private void after() {
                throw new RuntimeException();
            }
        });

        parser.parse(new String[] {});
    }

    @Test(expected = RuntimeException.class)
    public void testProtectedAfterParse() throws Exception {
        parser.addOptions(new Options() {
            @AfterParse
            protected void after() {
                throw new RuntimeException();
            }
        });

        parser.parse(new String[] {});
    }

    @Test(expected = RuntimeException.class)
    public void testPublicAfterParse() throws Exception {
        parser.addOptions(new Options() {
            @AfterParse
            public void after() {
                throw new RuntimeException();
            }
        });

        parser.parse(new String[] {});
    }
}
