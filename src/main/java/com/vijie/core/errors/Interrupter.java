package com.vijie.core.errors;

import com.vijie.core.interfaces.IToken;

/**
 * Represents an interrupter.
 * This class is used to handle interruptions with an associated token.
 */
public final class Interrupter extends RuntimeException {

    /**
     * The token associated with this interrupter.
     */
    private final IToken<?> token;

    /**
     * Constructs a new Interrupter instance.
     *
     * @param cause    The cause of the interruption, represented as a GenericInterrupter.
     * @param token    The token associated with this interrupter.
     */
    public Interrupter(GenericInterrupter cause, IToken<?> token) {
        super(cause.getMessage(), cause);
        this.token = token;
    }

    /**
     * Retrieves the cause of this interrupter.
     *
     * @return The cause of this interrupter, cast to a GenericInterrupter.
     */
    public GenericInterrupter getCause() {
        return (GenericInterrupter) super.getCause();
    }

    /**
     * Retrieves the token associated with this interrupter.
     *
     * @return The token associated with this interrupter.
     */
    public IToken<?> getToken() {
        return this.token;
    }

}
