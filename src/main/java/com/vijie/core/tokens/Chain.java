package com.vijie.core.tokens;


import com.vijie.core.NodeToken;
import com.vijie.core.Sequence;
import com.vijie.core.Utils;
import com.vijie.core.errors.*;
import com.vijie.core.interfaces.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a chain of specific tokens that can be parsed sequentially.
 * <p>
 * This abstract class provides a structure for parsing a sequence of tokens
 * using a predefined chain of parsers.
 *
 * @param <V> the type of the value returned by this token
 * @param <T> the type of the sub-tokens
 */
public abstract class Chain<V, T extends IToken<?>> extends NodeToken<V>
        implements IMultiTargetCompositeToken<V, T>, IMultiTokensCompositeToken<V, T> {

    /** The array of parsers in the chain. */
    protected final IParser<? extends T>[] targets;

    /**
     * Constructs a Chain with the specified parent, sequence, and chain of parsers.
     *
     * @param parent the parent composite
     * @param sequence the sequence to be parsed
     * @param targets the array of parsers in the chain
     */
    protected Chain(ICompositeToken<?> parent, Sequence sequence, IParser<? extends T>[] targets) {
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
    public T[] getTokens() {
        return (T[]) Arrays
                .stream(this.getContent())
                .filter(new Utils.filterTrims())
                .toArray(IToken[]::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <W> W[] getValues() {
        return (W[]) Arrays
                .stream(this.getContent())
                .filter(new Utils.filterTrims())
                .map(IToken::getValue)
                .toArray(Object[]::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void parse() throws BaseParseError {

        for (IParser<? extends T> target : this.targets) {

            try {
                this.sequence.parseAndStep(this, target);
            } catch (OptionalNotFound _) {}

        }

        this.sequence.clearFrom();

    }

}
