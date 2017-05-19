package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.DuplicateDriverError;
import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.OptionValue;
import cz.cuni.mff.yaclpplib.driver.Driver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HashDriverLocatorTest {

    private interface MyInterfaceA {}
    private interface MyInterfaceB {}

    private class MyType implements MyInterfaceA {}
    private class MySubtypeA extends MyType implements MyInterfaceB {}
    private class MySubtypeB extends MyType {}

    private class GenericTypeDriver<T> implements Driver<T> {
        final Class<T> returnType;

        private GenericTypeDriver(Class<T> returnType) {
            this.returnType = returnType;
        }

        @Override
        public T parse(OptionValue x) throws InvalidOptionValue {
            return null;
        }

        @Override
        public Class<T> getReturnType() {
            return returnType;
        }
    }

    private HashDriverLocator storage;

    @Before
    public void setUp() throws Exception {
        storage = new HashDriverLocator();
    }

    @Test
    public void testMultipleAdd() throws Exception {
        storage.add(new GenericTypeDriver<>(MyType.class));
        try {
            storage.add(new GenericTypeDriver<>(MyType.class));
            Assert.fail("DriverLocator should deny adding the same type.");
        } catch (DuplicateDriverError expected) { }

        storage.add(new GenericTypeDriver<>(MySubtypeA.class));
    }

    @Test
    public void testFindSpecific() throws Exception {
        Driver<MySubtypeA> mySubtypeDriver = new GenericTypeDriver<>(MySubtypeA.class);
        Driver<MyType> myTypeDriver = new GenericTypeDriver<>(MyType.class);

        storage.add(mySubtypeDriver);
        storage.add(myTypeDriver);

        Assert.assertEquals(storage.getDriverFor(MySubtypeA.class), mySubtypeDriver);
        Assert.assertEquals(storage.getDriverFor(MyType.class), myTypeDriver);
    }

    @Test
    public void testUnambiguity() throws Exception {
        Driver<MySubtypeA> mySubtypeDriver = new GenericTypeDriver<>(MySubtypeA.class);
        storage.add(mySubtypeDriver);
        Assert.assertEquals(storage.getDriverFor(MyInterfaceA.class), mySubtypeDriver);
        Assert.assertEquals(storage.getDriverFor(MyInterfaceB.class), mySubtypeDriver);
        Assert.assertEquals(storage.getDriverFor(MyType.class), mySubtypeDriver);
        Assert.assertEquals(storage.getDriverFor(MySubtypeA.class), mySubtypeDriver);

        Driver<MyType> myTypeDriver = new GenericTypeDriver<>(MyType.class);
        storage.add(myTypeDriver);
        Assert.assertEquals(storage.getDriverFor(MyInterfaceA.class), myTypeDriver);
        Assert.assertEquals(storage.getDriverFor(MyInterfaceB.class), mySubtypeDriver);
        Assert.assertEquals(storage.getDriverFor(MyType.class), myTypeDriver);
        Assert.assertEquals(storage.getDriverFor(MySubtypeA.class), mySubtypeDriver);
    }

    @Test(expected = AmbiguousDriverError.class)
    public void testAmbiguity() throws Exception {
        storage.add(new GenericTypeDriver<>(MySubtypeA.class));
        storage.add(new GenericTypeDriver<>(MySubtypeB.class));
        storage.getDriverFor(MyType.class);
    }

    @Test(expected = NoSuchDriverError.class)
    public void testNoSuchDriver() throws Exception {
        storage.add(new GenericTypeDriver<>(MyInterfaceA.class));
        storage.add(new GenericTypeDriver<>(MyInterfaceB.class));
        storage.add(new GenericTypeDriver<>(MySubtypeA.class));
        storage.getDriverFor(MySubtypeB.class);
    }
}