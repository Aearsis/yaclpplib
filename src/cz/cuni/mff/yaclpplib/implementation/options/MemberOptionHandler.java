package cz.cuni.mff.yaclpplib.implementation.options;


import cz.cuni.mff.yaclpplib.*;
import cz.cuni.mff.yaclpplib.annotation.Help;
import cz.cuni.mff.yaclpplib.annotation.Option;
import cz.cuni.mff.yaclpplib.implementation.OptionHandler;

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
    final private String help;

    /**
     * Constructs a new instance of the class for one specific member of {@link Options} instance.
     * @param parser parser requesting this handler
     * @param definitionClass instance of {@link Options} definins this member
     * @param from the field handled
     */
    MemberOptionHandler(ArgumentParser parser, Options definitionClass, AccessibleObject from) {
        this.parser = parser;
        this.definitionClass = definitionClass;

        annotations = from.getDeclaredAnnotationsByType(Option.class);
        assert annotations.length > 0;

        Help helpAnnotation = from.getDeclaredAnnotation(Help.class);

        /*
         * The appropriate behavior here depends on the use-case.
         * We considered forcing users to write help to every option,
         * but that'd make the library annoying to use in small personal projects.
         * In case the option is missing, we set a reasonable default,
         * which should be something visible and noticeable.
         */
        help = helpAnnotation != null ? helpAnnotation.value() : "do some magic";
    }

    @Override
    public String getHelpLine() {
        return String.format("  %-20s %s",
                String.join(", ",
                        Arrays.stream(annotations).map((x) -> x.help().equals("") ? x.value() : x.help())
                                .toArray(String[]::new)),
                help);
    }

    @Override
    public Options getDefinitionClass() {
        return definitionClass;
    }

    @Override
    public ArgumentParser getParser() {
        return parser;
    }

    @Override
    public void finish() {

    }

    @Override
    public String getAnyOptionName() {
        return annotations[0].value();
    }
}
