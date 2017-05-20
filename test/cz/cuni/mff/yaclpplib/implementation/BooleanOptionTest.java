package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.Options;
import org.junit.Test;

import static org.junit.Assert.*;

public class BooleanOptionTest {
    static class TestHandler implements OptionHandler, Options {
        public boolean value;

        @Override public ValuePolicy getValuePolicy(){
            return ValuePolicy.MANDATORY;
        }

        @Override
        public void setValue(Object typedValue, String optionName) {
            value = (boolean) typedValue;
        }

        @Override
        public Class<?> getType(){
            return Boolean.class;
        }

        @Override public ArgumentParser getParser() { return null; }
        @Override public String getAnyOptionName() { return "--option"; }
        @Override public Options getDefinitionClass() { return this; }
        @Override public String getHelpLine() { return ""; }
        @Override public void finish() { }
    }

    @Test
    public void testCasual() throws Exception {
        TestHandler testHandler = new TestHandler();
        BooleanOption booleanOption = new BooleanOption(testHandler);
        assertEquals(false, testHandler.value);

        booleanOption.setValue(true, "");
        assertEquals(true, testHandler.value);

        booleanOption.setValue(false, "");
        assertEquals(false, testHandler.value);
    }

    @Test
    public void testTricky() throws Exception {
        TestHandler testHandler = new TestHandler();
        BooleanOption booleanOption = new BooleanOption(testHandler);
        assertEquals(false, testHandler.value);

        booleanOption.setValue(null, "");
        assertEquals(true, testHandler.value);
    }

    @Test
    public void testOptionality() throws Exception {
        TestHandler testHandler = new TestHandler();
        BooleanOption booleanOption = new BooleanOption(testHandler);

        assertEquals(booleanOption.getValuePolicy(), ValuePolicy.OPTIONAL);
    }
}