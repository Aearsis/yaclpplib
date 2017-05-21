package cz.cuni.mff.yaclpplib.implementation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An utility class for working with primitives.
 * Its goal is to map primitive types to their wrapper types
 * in order to box them.
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

    /**
     * Boxes a primitive type. If the type given is not primitive, it does nothing.
     * @param type type being boxed
     * @return boxed type, or original type if not primitive
     */
    public static Class<?> box(Class<?> type) {
        if (!type.isPrimitive()) {
            return type;
        }

        return mapToBoxed.get(type);
    }

    private final static Set<Class<?>> integralTypes = new HashSet<Class<?>>() {{
        add(Long.class);
        add(Integer.class);
        add(Short.class);
        add(Byte.class);
    }};

    /**
     * Checks if the given type is an integral type.
     * @param type checked type
     * @return true if type is integral
     */
    public static boolean isIntegral(Class type) {
        final Class<?> boxed = box(type);
        return integralTypes.contains(boxed);
    }
}
