package com.vijie.core.errors;

import com.vijie.core.Sequence;
import com.vijie.core.interfaces.IParser;

/**
 * Exception thrown when an optional value is not found during parsing.
 */
public final class OptionalNotFound extends ParserError {

    /**
     * Constructs a new OptionalNotFound exception with the specified cause, parser, and sequence.
     *
     * @param sequence the sequence being parsed when the error occurred
     * @param cause    the cause of the exception
     * @param parser   the parser that encountered the error
     */
    public OptionalNotFound(Sequence sequence, ParserError cause, IParser<?> parser) {
        super(sequence, cause, parser, "Optional not found");
    }

    public ParserError getCause() {
        return (ParserError) super.getCause();
    }
}
