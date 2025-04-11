package com.vijie.core.tokens;

import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.parsers.Factory;

/**
 * Represents an enumeration of literals.
 * <p>
 * This abstract class extends the Union class to handle multiple literal types.
 *
 * @param <V> the type of the value
 * @param <T> the type of the literal, which extends Literal<V>
 */
public abstract class Enumeration<V, T extends Literal<V>> extends Union<V, T> {

    /**
     * Constructs an Enumeration instance.
     *
     * @param parent the parent composite
     * @param sequence the sequence
     * @param targets an array of parsers for the literals
     */
    public Enumeration(ICompositeToken<?> parent, Sequence sequence, Factory<? extends T>[] targets) {
        super(parent, sequence, targets);
    }

}

