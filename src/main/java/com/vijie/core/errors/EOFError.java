package com.vijie.core.errors;

import com.vijie.core.Sequence;

/**
 * Represents an error that occurs when an unexpected end of a sequence is encountered.
 */
public final class EOFError extends RuntimeException {

    private final Sequence sequence;

    /**
     * Constructs a new EOFError with the specified sequence.
     *
     * @param sequence the sequence that caused the error
     */
    public EOFError(Sequence sequence) {
        super("Unexpected end of sequence");
        this.sequence = sequence;
    }

    /**
     * Returns the sequence that caused the error.
     *
     * @return the sequence associated with this error
     */
    public Sequence getSequence() {
        return this.sequence;
    }
}
