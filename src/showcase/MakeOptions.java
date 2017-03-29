package showcase;

import cz.cuni.mff.yaclpplib.*;
import cz.cuni.mff.yaclpplib.annotation.*;

import java.util.List;

public class MakeOptions implements Options {

    // Allows multiple -D
    @Option("-D")
    public String[] definitions;

    public enum OutputSyncType {
        TARGET, LINE, RECURSE, NONE
    }

    @Option("-O")
    @Option("--output-sync")
    public OutputSyncType outputSyncType = OutputSyncType.TARGET;

    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParserFactory.create();
        final MakeOptions mo = parser.addOptions(new MakeOptions());

        final List<String> positional = parser.requestPositionalArguments();
        parser.parse(args);

        for (String s : positional)
            System.out.println(s);
    }
}
