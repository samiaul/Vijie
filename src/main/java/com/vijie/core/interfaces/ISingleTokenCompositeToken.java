package com.vijie.core.interfaces;

/**
 * Represents a composite token that contains a single sub-token.
 *
 * @param <V> the type of the value returned by this token
 * @param <W> the type of the value returned by the sub-token
 * @param <T> the type of the sub-token
 */
public interface ISingleTokenCompositeToken<V, W, T extends IToken<W>> extends ICompositeToken<V> {

    /**
     * Returns the sub-tokens.
     *
     * @return the sub-tokens
     */
    T getToken();

}
