package com.vijie.core.errors;

import com.vijie.core.Sequence;
import com.vijie.core.interfaces.IToken;

import java.util.List;

/**
 * This class represents an error that occurs when an optional chain is empty.
 */
public final class EmptyChainError extends MultiParseError {

    /**
     * Constructs an EmptyChainError.
     *
     * @param sequence The sequence where the error occurred.
     * @param types The list of target types that were expected but not parsed.
     * @param errors The array of specific parse errors encountered.
     */
    public EmptyChainError(Sequence sequence, List<Class<? extends IToken<?>>> types, ParserError[] errors) {
        super(sequence, types, errors, "Could not parse at least one target");
    }

}
