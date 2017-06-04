package parserTests;

import cz.cuni.mff.yaclpplib.*;
import org.junit.Before;
import org.junit.Test;
import showcase.SimulatorOptions;

import static org.junit.Assert.*;

public class SimulatorOptionsTest {
    private ArgumentParser parser;
    private SimulatorOptions options;

    @Before
    public void setUp() {
        options = new SimulatorOptions();
        parser = ArgumentParserFactory.create();
        parser.addOptions(options);
        parser.setUnexpectedParameterHandler(options.inputs::add);
    }

    @Test(expected = SimulatorOptions.UserException.class)
    public void testNoForceSelected() throws Exception {
        parser.parse("-i 5 --count=100 -s EXPLOSION input1".split(" "));
    }

    @Test(expected = SimulatorOptions.UserException.class)
    public void testMissingInput() throws Exception {
        parser.parse("-i 5 -c --count=100 -s EXPLOSION".split(" "));
    }

    @Test(expected = IllegalOptionValue.class)
    public void testOutOfRangeNegative() throws Exception {
        parser.parse("-i 5 -r --count=-3 -s EXPLOSION input1".split(" "));
    }

    @Test(expected = IllegalOptionValue.class)
    public void testOutOfRangeHigh() throws Exception {
        parser.parse("-i 5 -c --count=1000000 -s EXPLOSION input1".split(" "));
    }

    @Test(expected = MissingMandatoryOptionException.class)
    public void testMissingMandatory() throws Exception {
        parser.parse("-c --count=10000 -s EXPLOSION input1".split(" "));
    }

    @Test
    public void testEmptyDebug() throws Exception {
        parser.parse("-i 5 -c --count=1000 --debug -s EXPLOSION input1".split(" "));

        assertEquals(options.debugLevel, 10);
    }

    @Test
    public void testValueDebug() throws Exception {
        parser.parse("-i 5 -c --count=1000 --debug=20 -s explosion input1".split(" "));

        assertEquals(options.debugLevel, 20);
    }

    @Test
    public void testValidInput() throws Exception {
        parser.parse(("-i 10 -c -r --count=50 -s directional -s EXPLOSION -m 2.50 -d 0.1 " +
                "--output=logfile input1 input2 input3").split(" "));

        assertEquals(options.iterations, 10);
        assertEquals(options.compulsiveForce, true);
        assertEquals(options.repulsiveForce, true);
        assertEquals(options.particles, 50);
        assertEquals(options.simulations.length, 2);
        assertEquals(options.outputFile, "logfile");
        assertEquals(options.particleMass, 2.5, 0.0000001);
        assertEquals(options.decay, 0.1, 0.0000001);
        assertEquals(options.simulations[0], SimulatorOptions.SimulationType.DIRECTIONAL);
        assertEquals(options.simulations[1], SimulatorOptions.SimulationType.EXPLOSION);
        assertEquals(options.inputs.get(0), "input1");
        assertEquals(options.inputs.get(1), "input2");
        assertEquals(options.inputs.get(2), "input3");
    }

}