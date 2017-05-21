package cz.cuni.mff.yaclpplib.implementation;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * A list of tokens received as command line arguments.
 * This class is a wrapper around {@link Deque} which
 * exposes only required methods.
 */
public class TokenList {

    private final Deque<String> tokens;

    /**
     * Creates a new token list from read command line arguments.
     * @param args array of read arguments
     */
    public TokenList(String[] args) {
        tokens = new ArrayDeque<>(Arrays.asList(args));
    }

    /**
     * Returns the number of tokens in this token list.
     * @return the number of tokens
     */
    public int size() {
        return tokens.size();
    }

    /**
     * Retrieves and removes the first token in the list, or returns {@code null}, if empty.
     * @return first token or {@code null} if empty
     */
    public String remove() {
        return tokens.pollFirst();
    }

    /**
     * Retrieves, but does not remove, the first token in the list, or {@code null}, if empty.
     * @return first token or {@code null} if empty
     */
    public String peek() {
        return tokens.peekFirst();
    }
}
