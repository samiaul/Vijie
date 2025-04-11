package com.vijie.core.errors;

/**
 * Exception thrown when a literal string is empty.
 * This exception is used to indicate that a literal string
 * does not contain any characters.
 */
public class EmptyLiteralException extends RuntimeException {

    /**
     * Constructs a new EmptyLiteralException with no detail message or cause.
     */
    public EmptyLiteralException() {
        super("Literal string is empty");
    }
}