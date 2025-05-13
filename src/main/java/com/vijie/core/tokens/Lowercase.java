package com.vijie.core.tokens;


import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.parsers.Factory;

/**
 * Represents a lowercase character token.
 */
public final class Lowercase extends DefinedChar {

    /**
     * Creates a parser for the Lowercase class.
     *
     * @return a Factory instance for Lowercase
     */
    public static Factory<Lowercase> parser() {
        return Factory.of(Lowercase.class);
    }

    /**
     * Constructs a new Lowercase token.
     *
     * @param parent the parent composite
     * @param sequence the sequence of characters
     */
    public Lowercase(ICompositeToken<?> parent, Sequence sequence) {
        super(parent, sequence, "abcdefghijklmnopqrstuvwxyz");
    }

}
