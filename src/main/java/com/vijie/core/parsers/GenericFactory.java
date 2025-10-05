package com.vijie.core.parsers;

import com.vijie.core.Sequence;
import com.vijie.core.errors.BaseParseError;
import com.vijie.core.errors.GenericInterrupter;
import com.vijie.core.errors.Interruption;
import com.vijie.core.errors.ParserError;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.interfaces.IToken;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A factory class for creating tokens, holding parameters before instantiation.
 *
 * @param <T> the type of Composite token that this parser handles
 */
public abstract class GenericFactory<T extends ICompositeToken<?>> implements IParser<T> {

    protected abstract T instantiateToken(ICompositeToken<?> parent, Sequence sequence);

    /**
     * {@inheritDoc}
     */
    @Override
    public T parse(ICompositeToken<?> parent, Sequence sequence) throws BaseParseError {

        T token = this.instantiateToken(parent, sequence);

        try {
            token.parse();
        } catch (GenericInterrupter error) {
            token.getSequence().setPointer(0);
            throw new Interruption(error, token);
        } catch (ParserError error) {
            throw this.onParseError(token, error);
        }

        return token;
    }

    /**
     * Handles parse errors by throwing a new BaseParseError.
     *
     * @param token the token that caused the error
     * @param error the original error
     * @return a new BaseParseError
     */
    protected BaseParseError onParseError(T token, ParserError error) {
        return error;
    }

    /**
     * Gets the types of the tokens handled by the given parsers.
     *
     * @param parsers the parsers to get the token types from
     * @param <T>     the type of ICompositeToken that the parsers handle
     * @return a list of token types
     */
    @SafeVarargs
    public static <T extends IToken<?>> List<Class<? extends T>> getTypes(IParser<? extends T>... parsers) {
        return Arrays.stream(parsers).map(IParser::getType).collect(Collectors.toList());
    }

}
