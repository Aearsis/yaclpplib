package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.OptionValue;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ArrayOption extends OptionHandlerDecorator {

    static boolean isApplicable(OptionHandler handler) {
        // Disable having two or more layers of aggregating options
        return handler.getType().isArray();
    }

    List<Object> buffer = new ArrayList<>();

    public ArrayOption(OptionHandler decorated) {
        super(decorated);
    }

    @Override
    public void haveTypedValue(OptionValue optionValue, Object typedValue) {
        buffer.add(typedValue);
    }

    @Override
    public void finish() {
        Object array = Array.newInstance(getType(), buffer.size());
        for (int i = 0; i < buffer.size(); ++i)
            Array.set(array, i, buffer.get(i));
        decorated.haveTypedValue(null, array);
    }

    @Override
    public Class getType() {
        return decorated.getType().getComponentType();
    }

}
