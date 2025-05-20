package com.vijie.core;

import com.vijie.core.interfaces.IRootToken;
import com.vijie.core.interfaces.IToken;
import com.vijie.core.symbols.Atom;
import com.vijie.core.symbols.Symbol;

/**
 * Abstract class Root that extends CompositeToken and implements IRootToken.
 *
 * @param <V> the type of the value returned by this token
 */
public abstract class Root<V> extends CompositeToken<V> implements IRootToken<V> {


    /**
     * Constructor that initializes the Root with a Sequence.
     *
     * @param sequence the sequence to initialize the Root with
     */
    protected Root(Sequence sequence) {
        super(sequence);
    }

    /**
     * Constructor that initializes the Root with an array of Token.
     *
     * @param content the array of IToken to initialize the Root with
     */
    protected Root(IToken<?>[] content) {
        this(new Sequence(content));
    }

    /**
     * Constructor that initializes the Root with a raw string.
     * The string is tokenized into a Sequence.
     *
     * @param raw the raw string to initialize the Root with
     */
    protected Root(String raw) {
        this(Sequence.tokenize(raw));
    }

    /**
     * Gets the depth of the Root.
     *
     * @return the depth of the Root, which is always 0
     */
    public int getDepth() {
        return 0;
    }

    public void insert(int index, Character value) {

        Symbol<?> atom = this.getSequence().insert(index, value);

        Symbol<?> previous = this.getSequence().getSymbol(index - 1);
        Symbol<?> next = this.getSequence().getSymbol(index + 1);

    }
}
