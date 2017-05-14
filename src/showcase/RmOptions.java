package showcase;

import cz.cuni.mff.yaclpplib.annotation.*;
import cz.cuni.mff.yaclpplib.*;

import java.util.List;
import java.util.Optional;

public class RmOptions implements Options {

    @Option("-f")
    @Option("--force")
    @Help("ignore nonexistent files and arguments, never prompt")
    public boolean force = false;

    public enum Prompt {
        ALWAYS, ONCE, NEVER;
    }

    public Prompt prompt = null;

    @Option("-i")
    @Help("prompt before every removal")
    public void promptAlways() {
        prompt = Prompt.ALWAYS;
    }

    @Option("-I")
    @Help("prompt once before removing more than three files, or when removing recursively;" +
            " less intrusive than -i, while still giving protection against most mistakes")
    public void promptOnce() {
        prompt = Prompt.ONCE;
    }

    @Option(value = "--interactive", help = "--interactive=WHEN")
    @OptionalValue
    @Help("prompt according to WHEN: never, once (-I), or always (-i); without WHEN prompt always")
    public void interactive(Prompt value) {
        if(value != null) {
            prompt = value;
        }
        else {
            prompt = Prompt.ALWAYS;
        }
    }

    @Option("--one-file-system")
    @Help("when removing a hierarchy recursively, skip any directory " +
            "that is on a file system different from that of the corresponding command line argument")
    private boolean oneFileSystem = false;

    public boolean isOneFileSystem() {
        return oneFileSystem;
    }

    public boolean removeRoot = false;

    @Option("--preserve-root")
    @Help("do not remove '/' (default)")
    public void preserveRoot() {
        removeRoot = false;
    }

    @Option("--no-preserve-root")
    @Help("do not treat '/' specially")
    public void noPreserveRoot() {
        removeRoot = true;
    }

    @Option("-r")
    @Option("-R")
    @Option("--recursive")
    @Help("remove directories and their contents recursively")
    public boolean recursive = false;

    @Option("-d")
    @Option("--dir")
    @Help("remove empty directories")
    public boolean removeEmptyDirs = false;

    @Option("-v")
    @Option("--verbose")
    @Help("explain what is being done")
    public boolean verbose = false;

    @Option("--version")
    @Help("output version information and exit")
    public void displayVersion() {
        System.out.println("Version 1.0");
        System.exit(0);
    }

    public RmOptions() {

    }

    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParserFactory.create();
        RmOptions ro = parser.addOptions(new RmOptions());

        List<String> files = parser.requestPositionalArguments();
        parser.parse(args);

        for(String file : files) {
            System.out.println(file);
        }
    }
}
