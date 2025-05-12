package com.vijie.core.errors;

import com.vijie.core.interfaces.IToken;

/**
 * Represents an interrupter.
 * This class is used to handle interruptions with an associated token.
 */
public final class Interruption extends RuntimeException {

    /**
     * The token associated with this interrupter.
     */
    private final IToken<?> token;

    /**
     * Constructs a new Interruption instance.
     *
     * @param cause    The cause of the interruption, represented as a BaseParseError.
     * @param token    The token associated with this interrupter.
     */
    public Interruption(GenericInterrupter cause, IToken<?> token) {
        super(cause.getMessage(), cause);
        this.token = token;
    }

    /**
     * Retrieves the cause of this interrupter.
     *
     * @return The cause of this interrupter, cast to a BaseParseError.
     */
    public GenericInterrupter getCause() {
        return (GenericInterrupter) super.getCause();
    }

    /**
     * Retrieves the token associated with this interrupter.
     *
     * @return The token associated with this interrupter.
     */
    @SuppressWarnings("unchecked")
    public <T extends IToken<?>> T getToken() {
        return (T) this.token;
    }

    @Override
    public String toString() {
        return "Interruption(\"%s\", %s)".formatted(this.getCause().getMessage(), this.token);
    }
}
