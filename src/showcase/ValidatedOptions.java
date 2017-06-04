package showcase;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.ArgumentParserFactory;
import cz.cuni.mff.yaclpplib.InvalidArgumentsException;
import cz.cuni.mff.yaclpplib.Options;
import cz.cuni.mff.yaclpplib.annotation.Help;
import cz.cuni.mff.yaclpplib.annotation.Option;
import cz.cuni.mff.yaclpplib.annotation.Setup;

import static cz.cuni.mff.yaclpplib.validator.Utils.*;

/**
 * This class shows how validators can be used.
 *
 * The main advantage of using validators is that all error messages are shown at once.
 * Also, validator.Utils may ease checking of complex rules.
 */
public class ValidatedOptions implements Options {

    @Option("-a")
    @Help("Use feature A.")
    private boolean flagA;

    @Option("-b")
    @Help("Use feature B.")
    private boolean flagB;

    @Option("-c")
    @Help("Use feature C.")
    private boolean flagC;

    @Option("--answer-to-life-universe-and-everything")
    @Option("--answer")
    @Help("Answer the question of life, universe and everything.")
    private Integer answer = null;

    @Option("--force")
    @Help("use the force.")
    private boolean force = false;

    @Setup
    private void setupValidators(ArgumentParser parser) {
        parser.addValidator(exactlyOne(
                () -> flagA,
                () -> flagB,
                () -> flagC
        ), message("You must choose exactly one from -a, -b or -c."));

        parser.addValidator(imply(
                () -> answer != null,
                () -> answer == 42
        ), message("If you specify the answer, you must get it right, or use the force."));

        parser.addValidator(() -> force, message("Use the force, Luke."));
        parser.addValidator(() -> !force, message("One simply doesn't mess with the force."));
    }

    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParserFactory.create();
        ValidatedOptions validatedOptions = parser.addOptions(new ValidatedOptions());

        try {
            parser.parse(args);
        } catch (InvalidArgumentsException e) {
            System.err.println(e.getMessage());
        }
    }
}
