package cz.cuni.mff.yaclpplib.implementation;

import java.util.HashMap;
import java.util.Map;

/**
 * An utility class for working with primitives.
 */

public class Primitives {

    private final static Map<Class<?>, Class<?>> mapToBoxed = new HashMap<Class<?>, Class<?>>() {{
            put(boolean.class, Boolean.class);
            put(long.class, Long.class);
            put(int.class, Integer.class);
            put(short.class, Short.class);
            put(byte.class, Byte.class);
            put(char.class, Character.class);
            put(float.class, Float.class);
            put(double.class, Double.class);
            put(void.class, Void.class);
    }};

    public static Class<?> box(Class<?> type) {
        if (!type.isPrimitive())
            return type;

        return mapToBoxed.get(type);
    }

    final static Class<?>[] integralTypes = new Class[] { Long.class, Integer.class, Short.class, Byte.class };

    public static boolean isIntegral(Class type) {
        final Class<?> boxed = box(type);

        for (Class integral : integralTypes)
            if (integral.equals(boxed))
                return true;

        return false;
    }
}
