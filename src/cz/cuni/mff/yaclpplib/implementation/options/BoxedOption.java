package cz.cuni.mff.yaclpplib.implementation.options;

import cz.cuni.mff.yaclpplib.implementation.OptionHandler;
import cz.cuni.mff.yaclpplib.implementation.Primitives;

/**
 * Primitive types mess up the type system, so lets box them.
 * Fortunately, reflection boxes and unboxes automatically,
 * so this is effectively a noop-handler.
 */
public class BoxedOption extends OptionHandlerDecorator {

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

    public static OptionHandler wrapIfApplicable(OptionHandler handler) {
        if (isApplicable(handler)) {
            return new BoxedOption(handler);
        }
        else {
            return handler;
        }
    }
}
