package com.vijie.core.tokens;


import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.parsers.Factory;

/**
 * Represents an uppercase character token.
 */
public final class Uppercase extends DefinedChar {

    /**
     * Creates a factory for the Uppercase class.
     *
     * @return a Factory instance for Uppercase
     */
    public static Factory<Uppercase> parser() {
        return new Factory<>(Uppercase.class);
    }

    /**
     * Constructs an Uppercase token.
     *
     * @param parent the parent composite
     * @param sequence the sequence of characters
     */
    public Uppercase(ICompositeToken<?> parent, Sequence sequence) {
        super(parent, sequence, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

}
