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
abstract class MemberOptionHandler implements OptionHandler {

    final private ArgumentParser parser;
    final private Options definitionClass;

    // Initialized from annotations:
    final private Option[] annotations;
    final private boolean mandatory;
    final private String help;
    final private AccessibleObject handledObject;

    MemberOptionHandler(ArgumentParser parser, Options definitionClass, AccessibleObject from) {
        this.parser = parser;
        this.definitionClass = definitionClass;

        handledObject = from;
        annotations = from.getDeclaredAnnotationsByType(Option.class);
        assert annotations.length > 0;

        mandatory = from.getDeclaredAnnotation(Mandatory.class) != null;
        Help helpAnnotation = from.getDeclaredAnnotation(Help.class);

        /*
         * TODO: consider throwing InvalidSetupException.
         * Pros: always well-documented user programs
         * Cons: the need to write @Help _everywhere_
         */
        help = helpAnnotation != null ? helpAnnotation.value() : "Do some magic";
    }

    /**
     * TODO: move away to separate help formatter
     * @return
     */
    public String getHelpLine() {
        return String.format("  %-20s %s",
                String.join(", ",
                        Arrays.stream(annotations).map((x) -> x.help().equals("") ? x.value() : x.help())
                                .toArray(String[]::new)),
                help);
    }

    @Override
    public boolean isMandatory() {
        return mandatory;
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
