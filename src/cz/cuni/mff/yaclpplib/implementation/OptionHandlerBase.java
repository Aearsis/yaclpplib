package cz.cuni.mff.yaclpplib.implementation;


import cz.cuni.mff.yaclpplib.InvalidOptionValue;
import cz.cuni.mff.yaclpplib.MissingOptionValue;
import cz.cuni.mff.yaclpplib.OptionValue;
import cz.cuni.mff.yaclpplib.Options;
import cz.cuni.mff.yaclpplib.annotation.Help;
import cz.cuni.mff.yaclpplib.annotation.Mandatory;
import cz.cuni.mff.yaclpplib.annotation.Option;
import cz.cuni.mff.yaclpplib.driver.Driver;

import java.lang.*;
import java.lang.reflect.AccessibleObject;
import java.util.Arrays;

/**
 * A class handling one target of an option (field, method, etc.)
 */
abstract class OptionHandlerBase implements OptionHandler {

    final Options definitionClass;

    // Initialized from annotations:
    final Option[] annotations;
    final boolean mandatory;
    final private String help;

    boolean found = false;

    OptionHandlerBase(Options definitionClass, AccessibleObject from) {
        this.definitionClass = definitionClass;

        annotations = from.getDeclaredAnnotationsByType(Option.class);
        mandatory = from.getDeclaredAnnotation(Mandatory.class) != null;
        Help helpAnnotation = from.getDeclaredAnnotation(Help.class);
        help = helpAnnotation != null ? helpAnnotation.value() : "";
    }

    public String getHelpLine() {
        return String.format("%30s %s",
                String.join(", ", Arrays.stream(annotations).map((x) -> x.help().equals("") ? x.help() : x.value()).toArray(String[]::new)),
                help);
    }

    @Override
    public Options getDefinitionClass() {
        return definitionClass;
    }

    public void optionFound(OptionValue optionValue, Driver driver) {
        if (getValuePolicy() == ValuePolicy.MANDATORY && !optionValue.hasValue()) {
            throw new MissingOptionValue(optionValue);
        }
        if (getValuePolicy() == ValuePolicy.NEVER && optionValue.hasValue()) {
            throw new InvalidOptionValue("Parameter " + optionValue.getOption() + " cannot have an associated value.");
        }

        if (optionValue.hasValue()) {
            final Object typedValue = driver.parse(optionValue);
            haveTypedValue(optionValue, typedValue);
        }
        else {
            haveTypedValue(optionValue, null);
        }
    }

    public void finish() { }

}
