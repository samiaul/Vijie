package com.vijie.core.tokens;

import com.vijie.core.NodeToken;
import com.vijie.core.Sequence;
import com.vijie.core.Utils;
import com.vijie.core.errors.*;
import com.vijie.core.interfaces.*;

import java.util.Arrays;

/**
 * Represents an array of tokens.
 * <p>
 * This abstract class serves as a base for creating token arrays, where each token is parsed
 * using a specified parser. The array enforces constraints on the minimum and maximum
 * number of tokens it can contain.
 *
 * @param <V> the type of the value returned by this token
 * @param <T> the type of the sub-token(s)
 */
public abstract class Array<V, T extends IToken<?>> extends NodeToken<V> implements ISingleTargetCompositeToken<V, T> {

    /** The parser used to parse the tokens in the array. */
    protected final IParser<? extends T> target;

    /** The minimum number of tokens required in the array. */
    protected final int extentMin;

    /** The maximum number of tokens allowed in the array (0 indicates no limit). */
    protected final int extentMax;

    /**
     * Constructs an Array instance.
     *
     * @param parent the parent composite
     * @param sequence the sequence to be parsed
     * @param target the parser for the tokens
     */
    protected Array(ICompositeToken<?> parent,
                    Sequence sequence,
                    IParser<? extends T> target,
                    int extentMin,
                    int extentMax) throws IllegalExtentRangeException {

        super(parent, sequence);

        if (extentMin < 1 || (extentMax != 0 && extentMax < extentMin)) {
            throw new IllegalExtentRangeException(extentMin, extentMax);
        }

        this.target = target;
        this.extentMin = extentMin;
        this.extentMax = extentMax;
    }

    /**
     * Returns the minimum extent of the array.
     *
     * @return the minimum extent of the array
     */
    public int getExtentMin() {
        return this.extentMin;
    }

    /**
     * Returns the maximum extent of the array.
     *
     * @return the maximum extent of the array
     */
    public int getExtentMax() {
        return this.extentMax;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IParser<? extends T> getTarget() {
        return target;
    }

    /**
     * Return the tokens.
     *
     * @return the tokens
     */
    @SuppressWarnings("unchecked")
    public T[] getTokens() {
        return (T[]) this.getContent();
    }

    /**
     * Return an array of values of the targets.
     *
     * @return an array of values of the targets
     */
    @SuppressWarnings("unchecked")
    public <W extends V> W[] getValues() {
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

        while (this.loop()) {
            try {
                this.sequence.parseAndStep(this, this.target);
            } catch (OptionalNotFound _) {
            } catch (GenericParseError e) {
                break;
            }
        }

        if (!this.sequence.isEof()) this.sequence.clearFrom();

        if (this.getSize() < this.extentMin) throw new UndersizedArrayError(this.sequence);

    }

    /**
     * Determines whether the parsing loop should continue.
     *
     * @return {@code true} if the loop should continue, {@code false} otherwise
     */
    protected boolean loop() {
        if (this.sequence.isEof()) return false;
        if (this.extentMax == 0) return true;
        return !(this.sequence.getPointer() >= this.extentMax);
    }

}