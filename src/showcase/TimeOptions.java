package showcase;

import cz.cuni.mff.yaclpplib.*;
import cz.cuni.mff.yaclpplib.annotation.*;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TimeOptions implements Options {

    @Option("-f")
    @Option("--format")
    @Help("Specify output blablabla.")
    public String format = System.getenv("TIME");

    @Option("-p")
    @Option("--portability")
    @Help("Specify output blablabla.")
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
    private boolean append = false;

    @Validator
    public void validateAppendArgument() throws InvalidOptionValue {
        if (append && outputStream == System.out)
             throw new InvalidOptionValue("Append can be used only in conjuction with -o.");
    }

    @Option("-v")
    @Option("--verbose")
    boolean verbose = false;

    @Option("-V")
    @Option("--version")
    public void version() {
        System.out.println("0.0");
    }

    public TimeOptions() {
    }

}
