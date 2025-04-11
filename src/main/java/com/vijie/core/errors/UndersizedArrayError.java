package com.vijie.core.errors;

import com.vijie.core.Sequence;

/**
 * This class represents an error that occurs when an array is undersized.
 */
public final class UndersizedArrayError extends GenericParseError {

    /**
     * Constructs a new UndersizedArrayError with the specified sequence.
     *
     * @param sequence the sequence that caused the error
     */
    public UndersizedArrayError(Sequence sequence) {
        super(sequence, "Undersized Array");
    }
}
