package com.vijie.core.interfaces;

/**
 * Represents a composite token that can have multiple target parsers.
 *
 * @param <V> the type of the value returned by this token
 * @param <T> the type of the sub-token(s)
 */
public interface IMultiTargetCompositeToken<V, T extends IToken<?>> extends ICompositeToken<V> {

    /**
     * Returns the array of parsers used to parse the token.
     *
     * @return the array of parsers used to parse the token
     */
    IParser<? extends T>[] getTargets();
}
