package showcase;

import cz.cuni.mff.yaclpplib.annotation.*;
import cz.cuni.mff.yaclpplib.*;
import cz.cuni.mff.yaclpplib.validator.Utils;

import java.util.List;

public class HeadOptions implements Options {

    @Option("-c")
    @Option("--bytes")
    @Help("print the first NUM bytes of each file; with the leading '-'," +
            " print all but the last NUM bytes of each file")
    public int bytes = 0;

    @Option("-n")
    @Option("--lines")
    @Help("print the first NUM lines instead of the first 10; with the leading '-'," +
                    " print all but the last NUM lines of each file")
    public int lines = 10;

    @Option("-q")
    @Option("--quiet")
    @Option("--silent")
    @Help("never print headers giving file names")
    public boolean quiet = false;

    @Option("-v")
    @Option("--verbose")
    @Help("always print headers giving file names")
    public boolean verbose = false;

    @Option("-z")
    @Option("--zero-terminated")
    @Help("line delimiter is NUL, not newline")
    public boolean zeroTerminated = false;


    @Option("--version")
    @Help("output version information and exit")
    public void displayVersion() {
        System.out.println("Version 1.0");
        System.exit(0);
    }

    @Setup
    private void setupValidators(ArgumentParser parser) {
        parser.addValidator(() -> lines >= 1, Utils.message("The amount of lines must be a positive number."));
    }

    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParserFactory.create();
        HeadOptions ho = parser.addOptions(new HeadOptions());

        List<String> files = parser.requestPositionalArguments();
        try {
            parser.parse(args);
        } catch (InvalidArgumentsException e) {
            System.err.println(e.getMessage());
            return;
        }

        for(String file : files) {
            System.out.println(file);
        }
    }
}
