package cz.cuni.mff.yaclpplib.implementation;


import cz.cuni.mff.yaclpplib.*;
import cz.cuni.mff.yaclpplib.annotation.Help;
import cz.cuni.mff.yaclpplib.annotation.Mandatory;
import cz.cuni.mff.yaclpplib.annotation.Option;

import java.lang.*;
import java.lang.reflect.AccessibleObject;
import java.util.Arrays;

/**
 * A class handling one target of an option (field, method, etc.)
 */
abstract class OptionHandlerBase implements OptionHandler {

    final private ArgumentParser parser;
    final private Options definitionClass;

    // Initialized from annotations:
    final Option[] annotations;
    final boolean mandatory;
    final private String help;
    final private AccessibleObject handledObject;

    OptionHandlerBase(ArgumentParser parser, Options definitionClass, AccessibleObject from) {
        this.parser = parser;
        this.definitionClass = definitionClass;

        handledObject = from;
        annotations = from.getDeclaredAnnotationsByType(Option.class);
        assert annotations.length > 0;

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
    public boolean isMandatory() {
        return mandatory;
    }

    @Override
    public AccessibleObject getHandledObject() {
        return handledObject;
    }

    @Override
    public Options getDefinitionClass() {
        return definitionClass;
    }

    @Override
    public ArgumentParser getParser() {
        return parser;
    }

    public void finish() { }

    @Override
    public String getAnyOptionName() {
        return annotations[0].value();
    }
}
