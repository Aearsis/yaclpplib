package cz.cuni.mff.yaclpplib.implementation;


import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.MissingOptionValue;
import cz.cuni.mff.yaclpplib.OptionValue;
import cz.cuni.mff.yaclpplib.Options;
import cz.cuni.mff.yaclpplib.annotation.Help;
import cz.cuni.mff.yaclpplib.annotation.Mandatory;
import cz.cuni.mff.yaclpplib.annotation.Option;
import cz.cuni.mff.yaclpplib.annotation.OptionList;
import cz.cuni.mff.yaclpplib.driver.Driver;

import java.lang.*;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * A class handling one target of an option (field, method, etc.)
 */
abstract public class OptionHandler {

    final Options definitionClass;

    // Initialized from annotations:
    final Option[] annotations;
    final boolean mandatory;
    final private String help;

    boolean found = false;

    OptionHandler(Options definitionClass, AccessibleObject from) {
        this.definitionClass = definitionClass;

        annotations = from.getDeclaredAnnotationsByType(Option.class);
        mandatory = from.getDeclaredAnnotation(Mandatory.class) != null;
        Help helpAnnotation = from.getDeclaredAnnotation(Help.class);
        help = helpAnnotation != null ? helpAnnotation.value() : "";
    }

    String getHelpLine() {
        return String.format("%30s %s",
                String.join(", ", Arrays.stream(annotations).map((x) -> x.help().equals("") ? x.help() : x.value()).toArray(String[]::new)),
                help);

    }

    Class autoBox(Class type) {
        if (type == boolean.class) {
            return Boolean.class;
        }
        else if (type == int.class) {
            return Integer.class;
        }
        else if (type == long.class) {
            return Long.class;
        }
        else if (type == double.class) {
            return Double.class;
        }
        else if (type == float.class) {
            return Float.class;
        }
        else if (type == short.class) {
            return Short.class;
        }
        else if (type == byte.class) {
            return Byte.class;
        }
        else if (type == char.class) {
            return Character.class;
        }
        else if (type == void.class) {
            return Void.class;
        }
        return type;
    }

    void finish() {
        // TODO: override, when array, call method([]) / fill field
    }

    public void optionFound(OptionValue optionValue, Driver driver) {
        if (getValuePolicy() == ValuePolicy.MANDATORY && !optionValue.hasValue()) {
            throw new MissingOptionValue(optionValue);
        }
        if (getValuePolicy() == ValuePolicy.NEVER && optionValue.hasValue()) {
            throw new InvalidOptionValue("Parameter " + optionValue.getOption() + " cannot have an associated value.");
        }

        final Object typedValue = driver.parse(optionValue);
        haveTypedValue(optionValue, typedValue);
    }

    protected abstract void haveTypedValue(OptionValue optionValue, Object typedValue);

    public abstract Class getType();
    public abstract ValuePolicy getValuePolicy();

}
