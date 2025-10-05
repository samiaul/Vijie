package com.vijie.core.errors;

import com.vijie.core.interfaces.ICompositeToken;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static com.vijie.core.Utils.prependParamType;

/**
 * Represents an error that occurs during the instantiation of a token.
 * This error is thrown when a reflective operation fails while attempting
 * to create an instance of a class implementing the ICompositeToken interface.
 */
public final class TokenInstantiationError extends Error {

    /**
     * The class type of the token that caused the error.
     */
    private final Class<? extends ICompositeToken<?>> tokenType;

    /**
     * The parameters passed to the constructor of the token.
     */
    private final Object[] params;

    /**
     * Constructs a detailed error message based on the cause of the reflective operation failure,
     * the token type, and the provided parameters.
     *
     * @param cause The exception that caused the instantiation error.
     * @param token The class type of the token being instantiated.
     * @param params The parameters passed to the constructor of the token.
     * @return A formatted error message describing the failure.
     */
    private static String getErrorMessage(ReflectiveOperationException cause, Class<? extends ICompositeToken<?>> token, Object[] params) {

        return switch (cause) {
            case NoSuchMethodException _ ->
                    "No suitable constructor found for %s: %s | %s".formatted(token.getName(), Arrays.toString(prependParamType(params)),Arrays.toString(token.getConstructors()) );
            case InstantiationException _ ->
                    "Error while instantiating %s".formatted(token.getName());
            // case IllegalAccessException _ -> "";
            case null, default -> {
                assert cause != null;
                yield cause.toString();
            }
        };
    }

    /**
     * Constructs a new TokenInstantiationError with the specified cause, token type, and parameters.
     *
     * @param cause The exception that caused the instantiation error.
     * @param tokenType The class type of the token being instantiated.
     * @param params The parameters passed to the constructor of the token.
     */
    public TokenInstantiationError(ReflectiveOperationException cause, Class<? extends ICompositeToken<?>> tokenType, Object[] params) {
        super(getErrorMessage(cause, tokenType, params), cause);
        this.tokenType = tokenType;
        this.params = params;
    }

    /**
     * Gets the class type of the token that caused the error.
     *
     * @return The class type of the token.
     */
    public Class<? extends ICompositeToken<?>> getTokenType() {
        return this.tokenType;
    }

    /**
     * Gets the parameters that were passed to the constructor of the token.
     *
     * @return The parameters passed to the constructor.
     */
    public Object[] getParams() {
        return this.params;
    }

}
