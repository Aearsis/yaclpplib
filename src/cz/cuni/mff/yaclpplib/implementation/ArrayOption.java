package cz.cuni.mff.yaclpplib.implementation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ArrayOption extends OptionHandlerDecorator {

    static boolean isApplicable(OptionHandler handler) {
        // Disable having two or more layers of aggregating options
        return handler.getType().isArray();
    }

    private List<Object> buffer = new ArrayList<>();

    ArrayOption(OptionHandler decorated) {
        super(decorated);
    }

    @Override
    public void setValue(Object typedValue, String optionName) {
        buffer.add(typedValue);
    }

    @Override
    public void finish() {
        Object array = Array.newInstance(getType(), buffer.size());
        for (int i = 0; i < buffer.size(); ++i)
            Array.set(array, i, buffer.get(i));
        decorated.setValue(array, getAnyOptionName());
    }

    @Override
    public Class getType() {
        return decorated.getType().getComponentType();
    }

}
