package cz.cuni.mff.yaclpplib;

/**
 * Exception thrown when there are positional arguments present
 * and they were not requested by {@link ArgumentParser#requestPositionalArguments()}
 */
public class UnhandledArgumentException extends RuntimeException {

}
