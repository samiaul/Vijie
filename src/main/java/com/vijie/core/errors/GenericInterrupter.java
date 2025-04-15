package com.vijie.core.errors;

import com.vijie.core.Sequence;

/**
 * An abstract class representing a generic interrupter that extends RuntimeException.
 * It is used to handle interruptions with an associated sequence and message.
 */
public abstract class GenericInterrupter extends BaseParseError {

    /**
     * Constructs a new GenericInterrupter with the specified sequence and message.
     *
     * @param sequence the sequence associated with this interrupter
     * @param message the detail message
     */
    public GenericInterrupter(Sequence sequence, String message) {
        super(sequence.copy(), message);
    }

}
