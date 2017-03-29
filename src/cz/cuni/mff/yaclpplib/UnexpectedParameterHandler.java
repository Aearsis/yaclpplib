package cz.cuni.mff.yaclpplib;

import cz.cuni.mff.yaclpplib.UnhandledArgumentException;

public interface UnexpectedParameterHandler {
    void handle(String value) throws UnhandledArgumentException;
}
