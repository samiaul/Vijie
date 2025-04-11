package com.vijie.core.tokens;


import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.parsers.Factory;

/**
 * Represents a digit character (0-9).
 */
public final class Digit extends DefinedChar {

    /**
     * Creates a factory for the Digit class.
     *
     * @return a Factory instance for Digit
     */
    public static Factory<Digit> parser() {
        return new Factory<>(Digit.class);
    }

    /**
     * Constructs a Digit instance with the specified parent and sequence.
     *
     * @param parent the parent composite
     * @param sequence the sequence of characters
     */
    public Digit(ICompositeToken<?> parent, Sequence sequence) {
        super(parent, sequence, "0123456789");
    }

}
