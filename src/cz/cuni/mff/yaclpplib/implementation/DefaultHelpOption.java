package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.*;
import cz.cuni.mff.yaclpplib.annotation.*;

@Help("Default options")
public final class DefaultHelpOption implements cz.cuni.mff.yaclpplib.Options {

    @Option("--help")
    @Help("Print a usage on standard output and exit succesfully.")
    public void help(OptionValue value) {
        System.out.println(value.getParser().getHelp());
        System.exit(0);
    }
}
