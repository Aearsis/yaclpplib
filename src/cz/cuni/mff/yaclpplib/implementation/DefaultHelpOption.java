package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.annotation.*;

@Help("Default options")
/**
 * A help-ful class which adds --help option, which prints the help message and exits user's program.
 * Allows having --help without any real effort.
 */
public final class DefaultHelpOption implements cz.cuni.mff.yaclpplib.Options {

    private final ArgumentParser parser;

    public DefaultHelpOption(ArgumentParser parser) {
        this.parser = parser;
    }

    @Option("--help")
    @Help("Print a usage on standard output and exit successfully.")
    public void help() {
        parser.printHelp();
        System.exit(0);
    }
}
