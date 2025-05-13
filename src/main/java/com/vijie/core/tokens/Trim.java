package com.vijie.core.tokens;

import com.vijie.core.Sequence;
import com.vijie.core.interfaces.*;
import com.vijie.core.parsers.Factory;

/**
 * The Trim class is a composite token that trims specified
 * characters from the left and right sides of a target token.
 *
 * @param <V> the type of value returned by this token
 * @param <T> the type of the sub-token
 */
public class Trim<V, T extends IToken<V>> extends Chain<V, T>
        implements ISingleTargetCompositeToken<V, T>, ISingleTokenCompositeToken<V, V, T> {

    /**
     * Creates a factory for the Trim class with the specified target parser and left and right blacklist.
     *
     * @param target the target parser
     * @param leftBlacklist the characters to be trimmed from the left
     * @param rightBlacklist the characters to be trimmed from the right
     * @param <V> the type of value returned by the target parser
     * @param <T> the type of token being parsed
     * @return a Factory instance for Trim
     */
    public static <V, T extends IToken<V>> Factory<Trim<V, T>> parser(Factory<? extends T> target, String leftBlacklist, String rightBlacklist) {
        return Factory.of(Trim.class, target, leftBlacklist, rightBlacklist);
    }

    /**
     * Constructs a chain of parsers for trimming tokens.
     *
     * @param target the target parser
     * @param leftBlacklist the characters to be trimmed from the left
     * @param rightBlacklist the characters to be trimmed from the right
     * @param <T> the type of token being parsed
     * @return an array of parsers for trimming tokens
     */
    @SuppressWarnings("unchecked")
    private static <T extends IToken<?>> IParser<? extends T>[] constructChain(IParser<T> target, String leftBlacklist, String rightBlacklist) {
        return new IParser[] {
                Trimmed.parser(leftBlacklist),
                target,
                Trimmed.parser(rightBlacklist)
        };
    }

    /** The characters to be trimmed from the left. */
    protected String leftBlacklist;

    /** The characters to be trimmed from the right. */
    protected String rightBlacklist;

    /** The target parser. */
    protected IParser<? extends T> target;

    /**
     * Constructs a Trim instance.
     *
     * @param parent the parent composite
     * @param sequence the sequence
     * @param target the target parser
     * @param leftBlacklist the characters to be trimmed from the left
     * @param rightBlacklist the characters to be trimmed from the right
     */
    public Trim(ICompositeToken<?> parent, Sequence sequence, Factory<? extends T> target, String leftBlacklist, String rightBlacklist) {
        super(parent, sequence, Trim.constructChain(target, leftBlacklist, rightBlacklist));
        this.leftBlacklist = leftBlacklist;
        this.rightBlacklist = rightBlacklist;
        this.target = target;
    }

    /**
     * Returns the characters to be trimmed from the left.
     *
     * @return the characters to be trimmed from the left
     */
    public String getLeftBlacklist() {
        return leftBlacklist;
    }

    /**
     * Returns the characters to be trimmed from the right.
     *
     * @return the characters to be trimmed from the right
     */
    public String getRightBlacklist() {
        return rightBlacklist;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IParser<? extends T> getTarget() {
        return target;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends IToken<V>> getType() {
        return this.getToken().getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public T getToken() {
        if (this.getContent()[0].matches(Trimmed.class)) return (T) this.getContent()[1];
        else return (T) super.getContent()[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V getValue() {
        return this.getToken().getValue();
    }
}
