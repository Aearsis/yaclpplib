package cz.cuni.mff.yaclpplib.implementation.options;

import cz.cuni.mff.yaclpplib.implementation.OptionHandler;
import cz.cuni.mff.yaclpplib.implementation.Primitives;

/**
 * Primitive types mess up the type system, so lets box them.
 * Fortunately, reflection boxes and unboxes automatically,
 * so this is effectively a noop-handler.
 */
public class BoxedOption extends OptionHandlerDecorator {

    BoxedOption(OptionHandler decorated) {
        super(decorated);
    }

    @Override
    public Class getType() {
        return Primitives.box(super.getType());
    }

    private static boolean isApplicable(OptionHandler handler) {
        return handler.getType().isPrimitive();
    }

    /**
     * Wraps the handler into {@link BoxedOption} if needed
     * @param handler handler to wrap
     * @return wrapped handler, if needed, otherwise the original one
     */
    public static OptionHandler wrapIfApplicable(OptionHandler handler) {
        if (isApplicable(handler)) {
            return new BoxedOption(handler);
        }
        else {
            return handler;
        }
    }
}
