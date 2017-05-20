package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.IllegalOptionValue;
import cz.cuni.mff.yaclpplib.Options;
import cz.cuni.mff.yaclpplib.annotation.Range;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RangeOptionTest {

    static class TestHandler implements OptionHandler, Options {
        @Range(minimumValue = 42, maximumValue = 42)
        public Integer value;

        @Override public ValuePolicy getValuePolicy(){
            return ValuePolicy.MANDATORY;
        }

        @Override
        public void setValue(Object typedValue, String optionName) {
            value = (int) typedValue;
        }

        @Override
        public Class<?> getType(){
            return Integer.class;
        }

        @Override public ArgumentParser getParser() { return null; }
        @Override public String getAnyOptionName() { return "--option"; }
        @Override public Options getDefinitionClass() { return this; }
        @Override public String getHelpLine() { return ""; }
        @Override public void finish() { }
    }

    TestHandler testHandler;
    RangeOption rangeOption;

    @Before
    public void setUp() throws Exception {
        testHandler = new TestHandler();
        rangeOption = new RangeOption(testHandler, TestHandler.class.getField("value").getDeclaredAnnotation(Range.class));
    }

    @Test(expected = IllegalOptionValue.class)
    public void testTooLow() throws Exception {
        rangeOption.setValue(0, "");
    }

    @Test
    public void testOK() throws Exception {
        rangeOption.setValue(42, "");
        assertEquals((Integer) 42, testHandler.value);
    }

    @Test(expected = IllegalOptionValue.class)
    public void testTooHigh() throws Exception {
        rangeOption.setValue(100, "");
    }
}