package com.vijie.core.tokens;

import com.vijie.core.NodeToken;
import com.vijie.core.Sequence;
import com.vijie.core.errors.*;
import com.vijie.core.interfaces.*;
import com.vijie.core.parsers.Factory;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Union of tokens.
 * <p>
 * This class is used to parse a token that can be one of several types.
 * Unlike the `Any` parser that returns the first token found, this parser
 * must be overridden as its children represents a new unique token.
 *
 * @param <V> the type of the value returned by this token
 * @param <T> the type of the sub-token
 */
public abstract class Union<V, T extends IToken<V>> extends NodeToken<V>
        implements IMultiTargetCompositeToken<V, T>, ISingleTokenCompositeToken<V, V, T> {

    /**
     * The target parsers for this Union node.
     */
    protected final IParser<? extends T>[] targets;

    /**
     * Constructs a Union node.
     *
     * @param parent the parent composite
     * @param sequence the sequence
     * @param targets the target parsers
     */
    protected Union(ICompositeToken<?> parent, Sequence sequence, IParser<? extends T>[] targets) {
        super(parent, sequence);
        this.targets = targets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IParser<? extends T>[] getTargets() {
        return targets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public T getToken() {
        return ((T) this.getContent()[0]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V getValue() {
        return this.getToken().getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void parse() throws BaseParseError {

        List<ParserError> errors = new ArrayList<>();

        for (IParser<? extends T> target : targets) {
            try {
                this.sequence.parse(parent, target);
            } catch (OptionalNotFound _) {
            } catch (ParserError error) {
                errors.add(error);
                continue;
            }
            this.sequence.clearRemainder();
            return;
        }

        throw new UnionError(this, Factory.getTypes(this.targets), errors.toArray(ParserError[]::new));

    }
}
