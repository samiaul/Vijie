package com.vijie.core.errors;

import com.vijie.core.Sequence;
import com.vijie.core.interfaces.IParser;

/**
 * Represents an error that occurs during parsing.
 */
public class ParserError extends GenericParseError {

    private final IParser<?> parser;

    /**
     * Constructs a new ParserError with the specified message, parser, sequence, and cause.
     *
     * @param sequence the sequence being parsed
     * @param cause    the underlying cause of the error
     * @param parser   the parser that caused the error
     * @param message  the detail message
     */
    public ParserError(Sequence sequence, GenericParseError cause, IParser<?> parser, String message) {
        super(sequence, message, cause);
        this.parser = parser;
    }

    /**
     * Constructs a new ParserError with the specified parser, sequence, and cause.
     *
     * @param sequence the sequence being parsed
     * @param cause    the underlying cause of the error
     * @param parser   the parser that caused the error
     */
    public ParserError(Sequence sequence, GenericParseError cause, IParser<?> parser) {
        this(sequence, cause, parser, String.format("Could not parse '%s'", parser));
    }

    /**
     * Returns the parser that caused the error.
     *
     * @return the parser that caused the error
     */
    public IParser<?> getParser() {
        return this.parser;
    }

    public GenericParseError getSource() {
        if (this.getCause() instanceof ParserError previous) return previous.getSource();
        return this.getCause();
    }

    public boolean isSource(Class<? extends GenericParseError> type) {
        return this.getSource().getClass().isAssignableFrom(type);
    }

    /**
     * Sets the depth of the error in the error chain.
     *
     * @param depth the depth to set
     */
    @Override
    public void setDepth(int depth) {
        super.setDepth(depth);
        this.getCause().setDepth(depth + 1);
    }

    /**
     * Returns the previous error in the error chain.
     *
     * @return the previous error
     */
    public GenericParseError getCause() {
        return (GenericParseError) super.getCause();
    }

    /**
     * Returns the origin of the error chain.
     *
     * @return the origin of the error chain
     */
    @Override
    public GenericParseError getOrigin() {
        if (this.getCause() == null) return this;
        return this.getCause().getOrigin();
    }

    @Override
    public boolean isEof() {
        return this.getCause().isEof();
    }
    
    /**
     * Returns a string representation of the error chain with traceback information.
     *
     * @param d the depth for formatting
     * @return the string representation of the error chain
     */
    @Override
    public String getTraceback(int d) {
        String tab = '\n' + "\t".repeat(d + 1);
        return "%s%s".formatted(
                super.getTraceback(d),
                (this.getCause() != null) ? (tab + this.getCause().getTraceback(d + 1)) : "");
    }
}
