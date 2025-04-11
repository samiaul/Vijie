package com.vijie.core.tokens;


import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.interfaces.IToken;

/**
 * Represents a list of tokens tree.
 * <p>
 * This abstract class is a specialized version of the `Array` class, with undefined length.
 *
 * @param <V> the type of value held by the array
 * @param <T> the type of token contained in the array
 */
public abstract class List<V, T extends IToken<?>> extends Array<V, T> {

    /**
     * Constructs a new `List` instance.
     *
     * @param parent   the parent composite object
     * @param sequence the sequence associated with this list
     * @param target   the parser for the token type contained in the list
     */
    protected List(ICompositeToken<?> parent, Sequence sequence, IParser<? extends T> target) {
        super(parent, sequence, target, 1, 0);
    }

}