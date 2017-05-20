package cz.cuni.mff.yaclpplib.implementation;

import jdk.internal.org.objectweb.asm.tree.analysis.Value;
import org.junit.Test;

import static org.junit.Assert.*;

public class ValuePolicyTest {

    @Test
    public void testAcceptingValues() throws Exception {
        assertEquals(false, ValuePolicy.OPTIONAL.acceptsValue("--no"));
        assertEquals(false, ValuePolicy.OPTIONAL.acceptsValue("-n"));
        assertEquals(true, ValuePolicy.OPTIONAL.acceptsValue("value"));

        assertEquals(true, ValuePolicy.MANDATORY.acceptsValue("-n"));
        assertEquals(false, ValuePolicy.NEVER.acceptsValue("value"));
    }
}