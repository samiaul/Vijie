package com.vijie.core.errors;

import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.parsers.Any;
import com.vijie.core.parsers.Factory;

/**
 * Represents an error that occurs when none of the specified types can be parsed.
 */
public final class AnyParserError extends MultiParseError {

    /**
     * The parser instance that attempted to parse the input.
     */
    private final Any<?> parser;

    /**
     * Constructs an `AnyParserError` with a list of types, an array of errors, and a sequence.
     *
     * @param parser   The parser instance that attempted to parse the input.
     * @param sequence the sequence where the parsing errors occurred
     * @param errors   the array of errors that occurred during parsing
     */
    public AnyParserError(Any<? extends ICompositeToken<?>> parser, Sequence sequence, ParserError[] errors) {
        super(sequence, Factory.getTypes(parser.getTargets()), errors, "Could not parse %s".formatted(parser.getClass().getSimpleName()));
        this.parser = parser;
    }

    /**
     * Retrieves the parser instance that attempted to parse the input.
     *
     * @param <P> The type of the parser, extending from Any<?>.
     * @return The parser instance cast to the specified type.
     * @throws ClassCastException if the cast is invalid.
     */
    @SuppressWarnings("unchecked")
    public <P extends Any<?>> P getParser() {
        return (P) this.parser;
    }

    /**
     * Checks if the parser instance is of the specified type or a subclass of it.
     *
     * @param <P>  The type of the parser, extending from Any<?>.
     * @param type The class object representing the type to check against.
     * @return true if the parser instance is of the specified type or a subclass of it; false otherwise.
     */
    public <P extends Any<?>> boolean isParser(Class<P> type) {
        return this.parser.getClass().isAssignableFrom(type);
    }

}
