package cz.cuni.mff.yaclpplib.implementation;

import cz.cuni.mff.yaclpplib.InvalidArgumentsException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class ParserEventHandlerCollection {

    private final List<ParserEventHandler> eventHandlers = new ArrayList<>();

    /**
     * Add another handler.
     */
    public void add(ParserEventHandler parserEventHandler) {
        eventHandlers.add(parserEventHandler);
    }

    /**
     * Invoke all the handlers, return exceptions
     */
    Stream<InvalidArgumentsException> invoke() {
        return eventHandlers.stream().flatMap((handler) -> {
            try {
                handler.invoke();
                return Stream.empty();
            } catch (InvalidArgumentsException e) {
                return Stream.of(e);
            }
        });
    }
}
