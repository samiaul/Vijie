package com.vijie.core.errors;

import com.vijie.core.Sequence;

/**
 * Exception thrown when an unexpected end of sequence is encountered.
 */
public final class EOFParseError extends GenericParseError {

    /**
     * Constructs a new EOFParseError exception with the specified sequence.
     *
     * @param sequence the sequence that caused the exception
     */
    public EOFParseError(Sequence sequence) {
        super(sequence, "Unexpected end of sequence");
    }

    @Override
    public boolean isEof() {
        return true;
    }
}
