package com.vijie.core.tokens;


import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.parsers.Factory;


/**
 * Represents a numeric token composed of digits.
 */
public final class Numeric extends CharArray<String, Digit> {

    /**
     * Creates a factory for the Numeric class.
     *
     * @return a Factory instance for Numeric
     */
    public static Factory<Numeric> parser() {
        return Factory.of(Numeric.class);
    }

    /**
     * Constructs a Numeric token with the specified parent and sequence.
     *
     * @param parent the parent composite
     * @param sequence the sequence of characters
     */
    public Numeric(ICompositeToken<?> parent, Sequence sequence) {
        super(parent, sequence, Digit.parser(), 1, 0);
    }

    /**
     * Returns the value of the numeric token as a string.
     *
     * @return the joined string value of the numeric token
     */
    @Override
    public String getValue() {
        return this.getJoin();
    }

}

