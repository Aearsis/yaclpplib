package showcase;

import cz.cuni.mff.yaclpplib.*;
import cz.cuni.mff.yaclpplib.annotation.*;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TimeOptions implements Options {

    @Option("-f")
    @Option("--format")
    @Help("Specify output format, possibly overriding the format specified in the environment variable TIME.")
    public String format = System.getenv("TIME");

    @Option("-p")
    @Option("--portability")
    @Help("Use the portable output format.")
    public boolean portability = false;

    private OutputStream outputStream = System.out;

    @Option("-o")
    @Option("--output")
    @Help("Do not send the results to stderr, but overwrite the specified file.")
    public void setFile(String file) throws InvalidOptionValue {
        if (!Files.exists(Paths.get(file))) {
            throw new InvalidOptionValue("File does not exist.");
        }
    }

    @Option("-a")
    @Option("--append")
    @Help("(Used together with -o.) Do not overwrite but append.")
    private boolean append = false;

    @AfterParse
    public void validateAppendArgument() {
        if (append && outputStream == System.out)
             throw new IllegalOptionValue("Append can be used only in conjunction with -o.");
    }

    @Option("-v")
    @Option("--verbose")
    @Help("Give very verbose output about all the program knows about.")
    boolean verbose = false;

    @Option("-V")
    @Option("--version")
    @Help("Print version information on standard output, then exit successfully.")
    public void version() {
        System.out.println("0.0");
    }

    public TimeOptions() {

    }

    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParserFactory.create();
        final TimeOptions ta = parser.addOptions(new TimeOptions());

        final List<String> positional = parser.requestPositionalArguments();

        parser.parse(args);

        for (String s : positional) {
            System.out.println(s);
        }
    }
}
