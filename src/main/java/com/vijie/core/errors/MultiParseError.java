package com.vijie.core.errors;

import com.vijie.core.Sequence;
import com.vijie.core.interfaces.IToken;

import java.util.Arrays;
import java.util.List;

/**
 * Represents an error that occurs when none of the specified types can be parsed.
 */
public abstract class MultiParseError extends GenericParseError {

    /**
     * The list of types that were attempted to be parsed.
     */
    protected final List<Class<? extends IToken<?>>> types;

    /**
     * The array of errors that occurred during parsing.
     */
    private final ParserError[] errors;

    /**
     * Constructs an `MultiParseError` with a message, a list of types, an array of errors, and a sequence.
     *
     * @param sequence the sequence where the parsing errors occurred
     * @param types    the list of types that were attempted to be parsed
     * @param errors   the array of errors that occurred during parsing
     * @param message  the error message
     */
    public MultiParseError(Sequence sequence, List<Class<? extends IToken<?>>> types, ParserError[] errors, String message) {
        super(sequence, message);
        this.types = types;
        this.errors = errors;
        Arrays.stream(this.errors).forEach(e -> e.setDepth(depth + 1));
    }

    /**
     * Retrieves the array of errors that occurred during parsing.
     *
     * @return an array of `ParserError` objects representing the parsing errors.
     */
    public ParserError[] getErrors() {
        return this.errors;
    }

    /**
     * Retrieves the list of types that were attempted to be parsed.
     *
     * @return a list of `Class` objects representing the types that were attempted to be parsed.
     */
    public List<Class<? extends IToken<?>>> getTypes() {
        return this.types;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTraceback(int d) {
        String tab = "\n%s".formatted("\t".repeat(d));
        String errors = tab + String.join(tab, Arrays.stream(this.errors).map(e -> e.getTraceback(d + 1)).toArray(String[]::new));
        return "%s%s".formatted(super.getTraceback(d), errors
        );
    }
}
