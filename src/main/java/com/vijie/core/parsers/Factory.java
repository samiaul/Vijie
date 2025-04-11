package com.vijie.core.parsers;

import org.apache.commons.text.StringEscapeUtils;
import com.vijie.core.Sequence;
import com.vijie.core.errors.*;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.interfaces.IToken;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.vijie.core.Utils.*;

/**
 * A factory class for creating tokens, holding parameters before instantiation.
 *
 * @param <T> the type of Composite token that this parser handles
 */
public final class Factory<T extends ICompositeToken<?>> implements IParser<T> {

    /**
     * The class type of the token that this parser handles.
     */
    private final Class<T> tokenType;

    /**
     * Additional parameters for the token constructor.
     */
    private final Object[] params;

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
     * Instantiates a token of the specified type using the provided parent and sequence.
     *
     * @param parent   the parent composite
     * @param sequence the sequence to parse
     * @return an instance of the token type
     * @throws TokenInstantiationError if an error occurs during instantiation or if no suitable constructor is found
     */
    private T instantiateToken(ICompositeToken<?> parent, Sequence sequence) {

        try {
            return this.tokenType
                    .getConstructor(prependParamType(params))
                    .newInstance(prependParam(parent, sequence, params));
        } catch (NoSuchMethodException |
                 InstantiationException |
                 IllegalAccessException error) {
            throw new TokenInstantiationError(error, this.tokenType, this.params);
        } catch (InvocationTargetException error) {
            throw new RuntimeException(error.getCause());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public T parse(ICompositeToken<?> parent, Sequence sequence) throws BaseParseError {

        T token = instantiateToken(parent, sequence);

        try {
            token.parse();
        } catch (GenericInterrupter error) {
            if (!(error instanceof PendingInterrupter)) token.getSequence().clearRemainder();
            throw new Interrupter(error, token);
        }

        return token;
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

        return "{%s}%s".formatted(tokenType.getSimpleName(),params);
    }
}
