package com.vijie.core.interfaces;

/**
 * Represents a composite token that contains multiple sub-tokens.
 *
 * @param <V> the type of the value returned by this token
 * @param <T> the type of the sub-tokens
 */
public interface IMultiTokensCompositeToken<V, T extends IToken<?>> extends ICompositeToken<V> {

    /**
     * Returns the array of sub-tokens.
     *
     * @return the array of sub-tokens
     */
    T[] getTokens();

    /**
     * Returns an array of the sub-tokens values.
     *
     * @param <W> the type of the values returned by the sub-tokens
     * @return an array of the sub-tokens values
     */
    <W> W[] getValues();
}
