package com.vijie.core.tokens;


import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.parsers.Factory;

/**
 * Represents a digit character (0-9).
 */
public final class HexDigit extends DefinedChar {

    /**
     * Creates a factory for the Digit class.
     *
     * @return a Factory instance for Digit
     */
    public static Factory<HexDigit> parser() {
        return new Factory<>(HexDigit.class);
    }

    /**
     * Constructs a Digit instance with the specified parent and sequence.
     *
     * @param parent the parent composite
     * @param sequence the sequence of characters
     */
    public HexDigit(ICompositeToken<?> parent, Sequence sequence) {
        super(parent, sequence, "0123456789ABCDEFabcdef");
    }

}
