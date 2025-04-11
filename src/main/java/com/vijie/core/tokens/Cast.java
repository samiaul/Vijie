package com.vijie.core.tokens;

import com.vijie.core.NodeToken;
import com.vijie.core.Sequence;
import com.vijie.core.errors.BaseParseError;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.interfaces.ISingleTokenCompositeToken;
import com.vijie.core.interfaces.IToken;


/**
 * Defines a `Cast` token. This abstract class takes a target and
 * returns a different value type.
 *
 * @param <V> the type of the value returned by this token
 * @param <W> the type of the value returned by the sub-token
 * @param <T> the type of the sub-token
 */
public abstract class Cast<V, W, T extends IToken<W>>
        extends NodeToken<V> implements ISingleTokenCompositeToken<V, W, T> {

    /**
     * The parser used to parse the sub-token.
     */
    private final IParser<T> target;

    /**
     * Constructs an edge with the specified parent node and sequence.
     *
     * @param parent   the parent node
     * @param sequence the sequence associated with this edge
     */
    protected Cast(ICompositeToken<?> parent, Sequence sequence, IParser<T> target) {
        super(parent, sequence);
        this.target = target;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public T getToken() {
        return (T) this.getContent()[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void parse() throws BaseParseError {
        this.sequence.parseAndStep(this, this.target);
        this.sequence.clearFrom();
    }
}
