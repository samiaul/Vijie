package com.vijie.core.interfaces;

/**
 * Represents a composite token that has a single target parser.
 *
 * @param <V> the type of the value returned by this token
 * @param <T> the type of the sub-token(s)
 */
public interface ISingleTargetCompositeToken<V, T extends IToken<?>> extends ICompositeToken<V> {

    /**
     * Returns the parser used to parse the token.
     *
     * @return the parser used to parse the token
     */
    IParser<? extends T> getTarget();
}
