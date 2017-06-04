package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.InvalidArgumentsException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An {@link InvalidArgumentsException} that aggregates another exceptions,
 * to create neat summary of all what's wrong.
 */
class AggregatedInvalidArgumentsException extends InvalidArgumentsException {

    private final List<? extends InvalidArgumentsException> subExceptions;

    AggregatedInvalidArgumentsException(Stream<InvalidArgumentsException> subExceptions) {
        this.subExceptions = subExceptions.collect(Collectors.toList());
    }

    @Override
    public String getMessage() {
        final StringWriter message = new StringWriter();
        final PrintWriter messageWriter = new PrintWriter(message);
        messageWriter.println("There are several problems:");

        for (InvalidArgumentsException e : subExceptions) {
            messageWriter.println(" - " + e.getMessage());
        }

        return message.toString();
    }
}
