package cz.cuni.mff.yaclpplib.implementation;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class TokenList {

    private final Deque<String> tokens;

    public TokenList(String[] args) {
        tokens = new ArrayDeque<>(Arrays.asList(args));
    }

    public int size() {
        return tokens.size();
    }

    public String remove() {
        return tokens.pollFirst();
    }

    public String peek() {
        return tokens.peekFirst();
    }
}
