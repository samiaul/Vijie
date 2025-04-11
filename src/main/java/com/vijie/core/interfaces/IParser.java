package com.vijie.core.interfaces;

import com.vijie.core.Sequence;
import com.vijie.core.errors.BaseParseError;

/**
 *
 * A factory interface for creating tokens.
 * This interface defines methods for obtaining the type of token
 * and parsing a sequence to produce a token.
 *
 * @param <T> the type of token that this parser produces
 */
public interface IParser<T extends IToken<?>> {

    /**
     * Gets the type of token that this parser produces.
     *
     * @return the class type of the token
     */
    Class<? extends T> getType();

    /**
     * Parses a sequence and produces a token.
     *
     * @param sequence the sequence to parse
     * @return the parsed token
     */
    T parse(ICompositeToken<?> parent, Sequence sequence) throws BaseParseError;

}
