package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.MissingMandatoryOptionException;
import cz.cuni.mff.yaclpplib.annotation.Mandatory;
import cz.cuni.mff.yaclpplib.annotation.Option;
import cz.cuni.mff.yaclpplib.implementation.options.FieldOption;
import org.junit.Test;

import java.lang.reflect.Field;

public class MandatoryManagerTest {

    static class TestOptions {

        @Mandatory
        @Option("--do-run")
        boolean mandatory = false;

        @Option("--magic")
        boolean optional = false;
    }

    @Test(expected = MissingMandatoryOptionException.class)
    public void testThrow() throws Exception {
        MandatoryManager manager = new MandatoryManager();
        Field field = TestOptions.class.getDeclaredField("mandatory");
        manager.add(new FieldOption(null, null, field), field);
        manager.check();
    }

    @Test
    public void testBeQuiet() throws Exception {
        MandatoryManager manager = new MandatoryManager();
        Field field = TestOptions.class.getDeclaredField("mandatory");
        FieldOption handler = new FieldOption(null, null, field);
        manager.add(handler, field);
        manager.encountered(handler);
        manager.check();
    }

    @Test
    public void testEmpty() throws Exception {
        MandatoryManager manager = new MandatoryManager();
        manager.check();
    }
}