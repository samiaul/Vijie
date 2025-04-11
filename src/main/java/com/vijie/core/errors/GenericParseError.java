package com.vijie.core.errors;

import com.vijie.core.Sequence;


/**
 * Represents a generic parse error that extends the base parse error functionality.
 * This class serves as a base for specific parse error implementations.
 */
public abstract class GenericParseError extends BaseParseError {

    /**
     * The depth of the error in the error chain.
     */
    protected int depth;

    /**
     * Constructs a new GenericParseError with the specified message and sequence.
     *
     * @param sequence the sequence where the error occurred
     * @param message  the detail message
     */
    public GenericParseError(Sequence sequence, String message) {
        super(sequence, message);
        this.setDepth(0);
    }

    /**
     * Constructs a new GenericParseError with the specified message, sequence, and cause.
     *
     * @param sequence the sequence where the error occurred
     * @param message  the detail message
     * @param cause    the cause of the error
     */
    public GenericParseError(Sequence sequence, String message, GenericParseError cause) {
        super(sequence, message, cause);
        this.setDepth(1);
    }

    /**
     * Returns the depth of the error in the error chain.
     *
     * @return the depth of the error
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Sets the depth of the error in the error chain.
     *
     * @param depth the depth to set
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * Returns the origin of this parse error.
     * By default, it returns the current instance.
     *
     * @return the origin of this parse error
     */
    public GenericParseError getOrigin() {
        return this;
    }

    /**
     * Checks if the error is caused by reaching the end of the file (EOF).
     *
     * @return {@code false} as this error is not related to EOF by default
     */
    public boolean isEof() {
        return false;
    }

    /**
     * Returns a string representation of the error chain with traceback information.
     *
     * @param d the depth for formatting
     * @return the string representation of the error chain
     */
    public String getTraceback(int d) {
        return "%s: %s @ %s".formatted(
                this.getClass().getSimpleName(),
                this.getMessage(),
                this.getIndex());
    }

    /**
     * Returns a string representation of the error chain with default depth formatting.
     *
     * @return the string representation of the error chain
     */
    public String getTraceback() {
        return this.getTraceback(0);
    }

}