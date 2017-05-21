package parserTests;

import cz.cuni.mff.yaclpplib.ArgumentParserFactory;
import cz.cuni.mff.yaclpplib.Options;
import cz.cuni.mff.yaclpplib.annotation.Option;
import cz.cuni.mff.yaclpplib.implementation.ArgumentParserImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayTest {

    ArgumentParserImpl parser;

    @Before
    public void setUp() throws Exception {
        parser = (ArgumentParserImpl) ArgumentParserFactory.create();
    }

    @Test
    public void testMethodArray() throws Exception {
        final int[] length = new int[1];

        parser.addOptions(new Options() {
            @Option("-A")
            void testArray(String[] test) {
                length[0] = test.length;
            }
        });

        parser.parse("-A first -A second -A third".split(" "));

        assertEquals(3, length[0]);
    }

    private static class FieldArray implements Options {
        @Option("-s")
        @Option("--long")
        boolean[] fieldArray;
    }

    @Test
    public void testFieldArray() throws Exception {
        FieldArray fieldArray = new FieldArray();
        parser.addOptions(fieldArray);

        parser.parse("-strue -sfalse --long=0 --long=1".split(" "));

        assertEquals(4, fieldArray.fieldArray.length);
        assertEquals(true, fieldArray.fieldArray[0]);
        assertEquals(false, fieldArray.fieldArray[1]);
    }
}
