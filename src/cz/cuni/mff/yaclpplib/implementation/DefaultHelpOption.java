package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.annotation.*;

@Help("Default options")
public final class DefaultHelpOption implements cz.cuni.mff.yaclpplib.Options {

    private final ArgumentParser parser;

    public DefaultHelpOption(ArgumentParser parser) {
        this.parser = parser;
    }

    @Option("--help")
    @Help("Print a usage on standard output and exit succesfully.")
    public void help() {
        parser.printHelp();
        System.exit(0);
    }
}
