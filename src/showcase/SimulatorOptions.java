package showcase;

import cz.cuni.mff.yaclpplib.ArgumentParser;
import cz.cuni.mff.yaclpplib.ArgumentParserFactory;
import cz.cuni.mff.yaclpplib.InvalidArgumentsException;
import cz.cuni.mff.yaclpplib.Options;
import cz.cuni.mff.yaclpplib.annotation.*;
import cz.cuni.mff.yaclpplib.validator.Utils;

import java.util.ArrayList;
import java.util.List;

public class SimulatorOptions implements Options {

    public class UserException extends InvalidArgumentsException {
        UserException(String s) {
            super(s);
        }
    }

    public enum SimulationType {
        EXPLOSION, ENERGY_SURGE, DIRECTIONAL
    }

    @Option("-r")
    @Option("--repulsive")
    @Help("uses the repulsive force between particles")
    public boolean repulsiveForce = false;

    @Option("-c")
    @Option("--compulsive")
    @Help("uses the compulsive force between particles")
    public boolean compulsiveForce = false;

    public int debugLevel = 0;

    @Option("--debug")
    @Help("enables debug outputs")
    @OptionalValue
    public void enableDebug(Integer level) {
        if (level != null) {
            debugLevel = level;
        }
        else {
            debugLevel = 10;
        }
    }

    @Option("-m")
    @Option("--mass")
    @Help("the mass of the particles")
    public double particleMass = 1.0;

    @Option("-d")
    @Option("--decay")
    @Help("decay of particles speed")
    public double decay = 0.01;

    @Option("-i")
    @Option("--iterations")
    @Help("number of iterations calculated")
    @Range(minimumValue = 1, maximumValue = Integer.MAX_VALUE)
    @Mandatory
    public int iterations;

    @Option("-p")
    @Option("--count")
    @Help("amount of simulated particles")
    @Range(minimumValue = 1, maximumValue = 10000)
    @Mandatory
    public int particles;

    @Option("-s")
    @Option("--simulate")
    @Help("runs specified simulation")
    @Mandatory
    public SimulationType[] simulations;

    @Option("-o")
    @Option("--output")
    public String outputFile = null;

    // Positional arguments
    public List<String> inputs = new ArrayList<>();

    @Setup
    public void setupValidators(ArgumentParser parser) {
        parser.addValidator(() -> repulsiveForce || compulsiveForce, () -> new UserException("One of the forces must be enabled"));

        parser.addValidator(() -> inputs.size() > 0, () -> new UserException("No input files specified"));
    }

    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParserFactory.create();
        parser.addOptions(new SimulatorOptions());
        try {
            parser.parse(args);
        } catch (InvalidArgumentsException e) {
            System.err.println(e.getMessage());
            return;
        }
    }
}
