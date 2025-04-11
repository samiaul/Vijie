package com.vijie.core.errors;

import com.vijie.core.Sequence;

/**
 * Represents a base class for parse errors that occur during parsing.
 * This class extends `Exception` and provides additional context
 * about the sequence where the error occurred.
 */
public abstract class BaseParseError extends Exception {
    /**
     * The sequence where the error occurred.
     */
    protected final Sequence sequence;

    /**
     * Constructs a new `BaseParseError` with the specified sequence and message.
     *
     * @param sequence the sequence where the error occurred
     * @param message  the detail message
     */
    public BaseParseError(Sequence sequence, String message) {
        super(message);
        this.sequence = sequence;
    }

    /**
     * Constructs a new `BaseParseError` with the specified sequence, message, and cause.
     *
     * @param sequence the sequence where the error occurred
     * @param message  the detail message
     * @param cause    the cause of the error
     */
    public BaseParseError(Sequence sequence, String message, Throwable cause) {
        super(message, cause);
        this.sequence = sequence;
    }

    /**
     * Retrieves the sequence where the error occurred.
     *
     * @return the sequence associated with this error
     */
    public Sequence getSequence() {
        return sequence;
    }

    /**
     * Returns the current index in the sequence where the error occurred.
     *
     * @return the current index
     */
    public int getIndex() {
        return this.sequence.getCurrentIndex();
    }

}
