package com.vijie.core.interfaces;

import com.vijie.core.Sequence;
import com.vijie.core.errors.BaseParseError;
import com.vijie.core.errors.GenericFailedTokenError;

import java.util.List;

/**
 * Interface representing a composite structure that extends the IToken interface.
 * Composite tokens act as parent nodes that hold other sub-tokens.
 *
 * @param <V> the type of the value returned by this token
 */
public interface ICompositeToken<V> extends IToken<V> {

    /**
     * Gets the sequence associated with the composite.
     *
     * @return the sequence associated with the composite
     */
    Sequence getSequence();

    /**
     * Gets the content of the composite as an array of tokens.
     *
     * @return an array of tokens representing the content of the composite
     */
    IToken<?>[] getContent();

    /**
     * Gets the number of sub-tokens in the composite.
     *
     * @return the number of sub-tokens
     */
    int getSize();

    /**
     * Gets the errors associated with the composite token.
     *
     * @return a list of `GenericFailedTokenError` objects representing the errors
     */
    List<GenericFailedTokenError> getErrors();

    /**
     * Parses the token.
     * @throws BaseParseError if the parsing failed
     */
    void parse() throws BaseParseError;

}
