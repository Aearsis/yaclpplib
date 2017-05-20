package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.Options;
import cz.cuni.mff.yaclpplib.annotation.Option;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class ArrayOptionTest {

    static class TestHandler implements OptionHandler, Options {
        public int[] value;

        @Override public ValuePolicy getValuePolicy() {
            return ValuePolicy.OPTIONAL;
        }

        @Override
        public void setValue(Object typedValue, String optionName) {
            value = (int[]) typedValue;
        }

        @Override
        public Class<?> getType() {
            return int[].class;
        }

        @Override public ArgumentParser getParser() { return null; }
        @Override public String getAnyOptionName() { return "--option"; }
        @Override public Options getDefinitionClass() { return this; }
        @Override public String getHelpLine() { return ""; }
        @Override public void finish() { }
    }

    @Test
    public void testAggregation() throws Exception {
        TestHandler testHandler = new TestHandler();
        ArrayOption arrayOption = new ArrayOption(testHandler);

        arrayOption.setValue(10, "-o");
        arrayOption.setValue(42, "-o");
        arrayOption.setValue(10, "-o");

        assertEquals(null, testHandler.value);

        arrayOption.finish();

        assertEquals(3, testHandler.value.length);
    }

    @Test
    public void testEmptyArray() throws Exception {
        TestHandler testHandler = new TestHandler();
        ArrayOption arrayOption = new ArrayOption(testHandler);
        assertEquals(null, testHandler.value);
        arrayOption.finish();
        assertEquals(0, testHandler.value.length);
    }
}