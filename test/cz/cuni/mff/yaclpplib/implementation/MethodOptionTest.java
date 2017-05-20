package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.ArgumentParserFactory;
import cz.cuni.mff.yaclpplib.InvalidSetupError;
import cz.cuni.mff.yaclpplib.Options;
import cz.cuni.mff.yaclpplib.annotation.Option;
import cz.cuni.mff.yaclpplib.annotation.OptionalValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MethodOptionTest {

    static class TestingOptions implements Options {
        @Option("--testoption")
        void validWithoutArgs() { }

        @Option("--testoption")
        void validWithArg(boolean a) { }

        @Option("--testoption")
        void invalidMoreArguments(int a, int b) {}

        @Option("--testoption")
        @OptionalValue
        void invalidOptionalWithoutArgs() {}

        @Option("--testoption")
        @OptionalValue
        void invalidOptionalPrimitive(int a) {}

        @Option("--testoption")
        @OptionalValue
        void invalidOptionalPrimitiveArray(int[] a) {}
    };

    private MethodOption constructFromTesting(String methodName, Class<?>... args) throws NoSuchMethodException {
        return new MethodOption(null, null,
                TestingOptions.class.getDeclaredMethod(methodName, args));
    }


    public void testWithoutArgs() throws Exception {
        constructFromTesting("validWithoutArgs");
    }

    public void testWithOneArg() throws Exception {
        constructFromTesting("validWithArg", boolean.class);
    }

    @Test(expected = InvalidSetupError.class)
    public void testInvalidMoreArguments() throws Exception {
        constructFromTesting("invalidMoreArguments", int.class, int.class);
    }

    @Test(expected = InvalidSetupError.class)
    public void testInvalidOptionalWithoutArgs() throws Exception {
        constructFromTesting("invalidOptionalWithoutArgs");
    }

    @Test(expected = InvalidSetupError.class)
    public void testInvalidOptionalPrimitive() throws Exception {
        constructFromTesting("invalidOptionalPrimitive", int.class);
    }

    @Test(expected = InvalidSetupError.class)
    public void testInvalidOptionalPrimitiveArray() throws Exception {
        constructFromTesting("invalidOptionalPrimitiveArray", int[].class);
    }
}