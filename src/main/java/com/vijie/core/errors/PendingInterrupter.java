package com.vijie.core.errors;

import com.vijie.core.Sequence;

/**
 * Represents an abstract interrupter that is pending execution.
 * Extends the functionality of the GenericInterrupter class.
 */
public abstract class PendingInterrupter extends GenericInterrupter {

    /**
     * Constructs a PendingInterrupter with the specified sequence and message.
     *
     * @param sequence the sequence associated with the interrupter
     * @param message  the message describing the interrupter
     */
    public PendingInterrupter(Sequence sequence, String message) {
        super(sequence, message);
    }

}
