package com.vijie.core.parsers;

import com.vijie.core.CompositeToken;
import org.apache.commons.text.StringEscapeUtils;
import com.vijie.core.Sequence;
import com.vijie.core.errors.*;
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
public class Factory<T extends ICompositeToken<?>> implements IParser<T> {

    /**
     * The class type of the token that this parser handles.
     */
    protected final Class<T> tokenType;

    /**
     * Additional parameters for the token constructor.
     */
    protected Object[] params;

    /**
     * Constructs a new Factory instance.
     *
     * @param tokenType the class type of the token
     * @param params    additional parameters for the token constructor
     */
    public Factory(Class<T> tokenType, Object... params) {
        this.tokenType = tokenType;
        this.params = params;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<T> getType() {
        return this.tokenType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T parse(ICompositeToken<?> parent, Sequence sequence) throws BaseParseError {

        T token = CompositeToken.instantiate(this.tokenType, parent, sequence, this.params);

        try {
            token.parse();
        } catch (GenericInterrupter error) {
            //if (!(error instanceof FatalInterrupter)) token.getSequence().clearRemainder();
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

    @Override
    public String toString() {

        String params;

        if (this.params.length > 0) {
            params = Arrays.stream(this.params)
                    .map(String::valueOf)
                    .map(StringEscapeUtils::escapeJava)
                    .collect(Collectors.joining(", ", "(", ")"));
        } else {
            params = "";
        }

        return "{%s}%s".formatted(tokenType.getSimpleName(), params);
    }
}
