package com.vijie.core;

import com.vijie.core.errors.BaseParseError;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IToken;

import java.util.Arrays;
import java.util.stream.Collectors;


/**
 * An abstract class representing a composite token.
 * Composite tokens act as parent nodes that hold other sub-tokens.
 *
 * @param <V> the type of the value returned by this token
 */
public abstract class CompositeToken<V> extends Token<V> implements ICompositeToken<V> {

    /**
     * The sequence of tokens that this composite represents.
     */
    protected final Sequence sequence;

    /**
     * Constructs a new CompositeToken with the given sequence of tokens.
     *
     * @param sequence the sequence of tokens
     */
    protected CompositeToken(Sequence sequence) {
        this.sequence = sequence;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRaw() {
        return String.join("", Arrays.stream(this.getContent()).map(IToken::getRaw).toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIndex() {
        return this.getContent()[0].getIndex();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLength() {
        return Arrays.stream(this.getContent()).mapToInt(IToken::getLength).sum();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Sequence getSequence() {
        return this.sequence;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IToken<?>[] getContent() {
        return this.sequence.getContent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return this.sequence.getSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract V getValue();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void parse() throws BaseParseError;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTreeRepr() {
        String children = Arrays.stream(this.getContent())
                .map(IToken::getTreeRepr)
                .reduce("", (a, b) -> a + "\n" + b);
        return "%s%s".formatted(super.getTreeRepr(), children);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSequenceRepr() {
        return "<" + this + ">" + "[" + Arrays.stream(this.getContent()).map(IToken::getSequenceRepr).collect(Collectors.joining(", ")) + "]";
    }

    @Override
    public String toString() {
        if (this.sequence.isEof()) {
            try {
                return "%s(%s)@%d".formatted(this.getClass().getSimpleName(), this.getValue(), this.getIndex());
            } catch (Exception _) {
                return "%s(!)@%d".formatted(this.getClass().getSimpleName(), this.getIndex());
            }
        }
        return "%s(?)@%d".formatted(this.getClass().getSimpleName(), this.getIndex());
    }
}