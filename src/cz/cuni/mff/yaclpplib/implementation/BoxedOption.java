package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.OptionValue;
import cz.cuni.mff.yaclpplib.Options;
import cz.cuni.mff.yaclpplib.driver.Driver;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Primitive types mess up the type system, so lets box them.
 * Fortunately, reflection boxes and unboxes automatically,
 * so this is effectively a noop-handler.
 */
class BoxedOption extends OptionHandlerDecorator {

    static boolean isApplicable(OptionHandler handler) {
        return handler.getType().isPrimitive();
    }

    BoxedOption(OptionHandler decorated) {
        super(decorated);
    }

    @Override
    public Class getType() {
        return Primitives.box(super.getType());
    }
}
