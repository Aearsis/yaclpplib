package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.DuplicateDriverError;
import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.OptionValue;
import cz.cuni.mff.yaclpplib.driver.Driver;
import org.junit.Assert;
import org.junit.Test;

public class HashDriverStorageTest {

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

    @Test
    public void testMultipleAdd() throws Exception {
        final DriverStorage storage = new HashDriverStorage();

        storage.add(new GenericTypeDriver<>(MyType.class));
        try {
            storage.add(new GenericTypeDriver<>(MyType.class));
            Assert.fail("DriverStorage should deny adding the same type.");
        } catch (DuplicateDriverError expected) { }

        storage.add(new GenericTypeDriver<>(MySubtypeA.class));
    }

    @Test
    public void testFindSpecific() throws Exception {
        final DriverStorage storage = new HashDriverStorage();

        Driver<MySubtypeA> mySubtypeDriver = new GenericTypeDriver<>(MySubtypeA.class);
        Driver<MyType> myTypeDriver = new GenericTypeDriver<>(MyType.class);

        storage.add(mySubtypeDriver);
        storage.add(myTypeDriver);

        Assert.assertEquals(storage.find(MySubtypeA.class), mySubtypeDriver);
        Assert.assertEquals(storage.find(MyType.class), myTypeDriver);
    }

    @Test
    public void testUnambiguity() throws Exception {
        final DriverStorage storage = new HashDriverStorage();

        Driver<MySubtypeA> mySubtypeDriver = new GenericTypeDriver<>(MySubtypeA.class);
        storage.add(mySubtypeDriver);
        Assert.assertEquals(storage.find(MyInterfaceA.class), mySubtypeDriver);
        Assert.assertEquals(storage.find(MyInterfaceB.class), mySubtypeDriver);
        Assert.assertEquals(storage.find(MyType.class), mySubtypeDriver);
        Assert.assertEquals(storage.find(MySubtypeA.class), mySubtypeDriver);

        Driver<MyType> myTypeDriver = new GenericTypeDriver<>(MyType.class);
        storage.add(myTypeDriver);
        Assert.assertEquals(storage.find(MyInterfaceA.class), myTypeDriver);
        Assert.assertEquals(storage.find(MyInterfaceB.class), mySubtypeDriver);
        Assert.assertEquals(storage.find(MyType.class), myTypeDriver);
        Assert.assertEquals(storage.find(MySubtypeA.class), mySubtypeDriver);
    }

    @Test
    public void testAmbiguity() throws Exception {
        final DriverStorage storage = new HashDriverStorage();

        storage.add(new GenericTypeDriver<>(MySubtypeA.class));
        storage.add(new GenericTypeDriver<>(MySubtypeB.class));
        try {
            storage.find(MyType.class);
            Assert.fail();
        } catch (AmbiguousDriverError expected) {}
    }

    @Test
    public void testNoSuchDriver() throws Exception {
        final DriverStorage storage = new HashDriverStorage();

        storage.add(new GenericTypeDriver<>(MyInterfaceA.class));
        storage.add(new GenericTypeDriver<>(MyInterfaceB.class));
        storage.add(new GenericTypeDriver<>(MySubtypeA.class));
        try {
            storage.find(MySubtypeB.class);
            Assert.fail();
        } catch (NoSuchDriverError expected) {}
    }
}