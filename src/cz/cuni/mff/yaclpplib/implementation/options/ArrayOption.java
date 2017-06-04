package cz.cuni.mff.yaclpplib.implementation.options;

import cz.cuni.mff.yaclpplib.IllegalOptionValue;
import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.implementation.OptionHandler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * A decorator for array options, which fills a list instead of setting values directly.
 * After parsing, it converts the list into an array and sets user's array
 * to all parsed values.
 */
public class ArrayOption extends OptionHandlerDecorator {

    private final List<Object> buffer = new ArrayList<>();

    ArrayOption(OptionHandler decorated) {
        super(decorated);
    }

    @Override
    public void setValue(Object typedValue, String optionName) {
        buffer.add(typedValue);
    }

    @Override
    public void finish() throws IllegalOptionValue, InvalidOptionValue {
        final Object array = Array.newInstance(getType(), buffer.size());
        for (int i = 0; i < buffer.size(); ++i) {
            Array.set(array, i, buffer.get(i));
        }
        decorated.setValue(array, getAnyOptionName());
    }

    @Override
    public Class getType() {
        return decorated.getType().getComponentType();
    }

    /**
     * Wraps the handler into an {@link ArrayOption} decorator if the handler represents an array
     * @param handler handler being wrapped
     * @return wrapped handler if needed, otherwise the original one
     */
    public static OptionHandler wrapIfApplicable(OptionHandler handler) {
        if (handler.getType().isArray()) {
            return new ArrayOption(handler);
        }
        else {
            return handler;
        }
    }
}
