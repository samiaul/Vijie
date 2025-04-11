package com.vijie.core.errors;

import com.vijie.core.Sequence;
import com.vijie.core.interfaces.IToken;

/**
 * Represents an error that occurs when an expected glyph is not found.
 */
public final class ExpectedGlyphError extends GenericParseError {

    /**
     * The token that caused the error.
     */
    private final IToken<?> token;

    /**
     * Returns the token that caused the error.
     *
     * @return the token that caused the error
     */
    public IToken<?> getToken() {
        return this.token;
    }

    /**
     * Constructs a new ExpectedGlyphError with the specified token and sequence.
     *
     * @param sequence the sequence in which the error occurred
     * @param token    the token that caused the error
     */
    public ExpectedGlyphError(Sequence sequence, IToken<?> token) {
        super(sequence, "Expected Char, found: %s".formatted(token));
        this.token = token;
    }
}
