package com.vijie.core.errors;

import com.vijie.core.interfaces.IToken;
import com.vijie.core.tokens.Union;

import java.util.List;

/**
 * Represents an error that occurs when none of the specified types can be parsed.
 */
public final class UnionError extends MultiParseError {

    /**
     * The token instance that attempted to parse the input.
     */
    private final Union<?, ?> token;

    /**
     * Constructs a new UnionError instance.
     *
     * @param token  the Union token that attempted to parse the input
     * @param types  the list of types that were attempted to be parsed
     * @param errors the array of errors that occurred during parsing
     */
    public UnionError(Union<?, ?> token, List<Class<? extends IToken<?>>> types, ParserError[] errors) {
        super(token.getSequence(), types, errors, "Could not parse %s".formatted(token.getClass().getSimpleName()));
        this.token = token;
    }

    /**
     * Retrieves the token associated with this error.
     *
     * @param <T> the type of the token, which extends Union
     * @return the token cast to the specified type
     * @throws ClassCastException if the token cannot be cast to the specified type
     */
    @SuppressWarnings("unchecked")
    public <T extends Union<?, ?>> T getToken() {
        return (T) this.token;
    }

}
