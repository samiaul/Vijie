package com.vijie.core.errors;

import com.vijie.core.Sequence;

/**
 * Represents an error that occurs when an invalid character is encountered during parsing.
 */
public final class InvalidCharError extends GenericParseError {

    /**
     * The actual character found.
     */
    private final Character actual;

    /**
     * The expected characters.
     */
    private final String expected;

    /**
     * Constructs a new InvalidCharError with the specified actual character, expected characters, and sequence.
     *
     * @param sequence the sequence where the error occurred
     * @param expected the expected characters
     * @param actual   the actual character found
     */
    public InvalidCharError(Sequence sequence, String expected, Character actual) {
        super(sequence, "Expected char(s) \"%s\", found: '%s'".formatted(expected, actual));
        this.actual = actual;
        this.expected = expected;
    }

    /**
     * Gets the actual character found.
     *
     * @return the actual character
     */
    public Character getActual() {
        return actual;
    }

    /**
     * Gets the expected characters.
     *
     * @return the expected characters
     */
    public String getExpected() {
        return expected;
    }
}
