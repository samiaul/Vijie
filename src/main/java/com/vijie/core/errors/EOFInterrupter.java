package com.vijie.core.errors;

import com.vijie.core.Sequence;

/**
 * Exception thrown when an unexpected end of sequence is encountered.
 */
public final class EOFInterrupter extends GenericInterrupter {

    /**
     * Constructs a new EOFParseError exception with the specified sequence.
     *
     * @param sequence the sequence that caused the exception
     */
    public EOFInterrupter(Sequence sequence) {
        super(sequence, "Unexpected end of sequence");
    }

}
