package showcase;

import cz.cuni.mff.yaclpplib.Options;
import cz.cuni.mff.yaclpplib.annotation.Option;

public class MakeOptions implements Options {

    @Option("-D")
    public String[] definitions;

    public enum OutputSyncType {
        TARGET, LINE, RECURSE, NONE
    }

    @Option("-O")
    @Option("--output-sync")
    public OutputSyncType outputSyncType = OutputSyncType.TARGET;

}
