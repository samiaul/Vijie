package com.vijie.core;

import com.vijie.core.errors.BaseParseError;
import com.vijie.core.errors.TokenInstantiationError;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IToken;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.vijie.core.Utils.prependParam;
import static com.vijie.core.Utils.prependParamType;


/**
 * An abstract class representing a composite token.
 * Composite tokens act as parent nodes that hold other sub-tokens.
 *
 * @param <V> the type of the value returned by this token
 */
public abstract class CompositeToken<V> extends Token<V> implements ICompositeToken<V> {

    /**
     * Instantiates a token of the specified type using the provided parent and sequence.
     * <p>
     * This method uses reflection to find and invoke a constructor of the specified token type.
     * It prepends the parent and sequence parameters to the provided additional parameters
     * and attempts to create a new instance of the token type.
     *
     * @param <T>       the type of the composite token to instantiate
     * @param tokenType the class type of the token to instantiate
     * @param parent    the parent composite token
     * @param sequence  the sequence to parse
     * @param params    additional parameters to pass to the constructor
     * @return an instance of the specified token type
     * @throws TokenInstantiationError if an error occurs during instantiation or if no suitable constructor is found
     * @throws RuntimeException        if an exception is thrown by the constructor
     */
    public static <T extends ICompositeToken<?>> T instantiate(Class<T> tokenType, ICompositeToken<?> parent, Sequence sequence, Object... params) {

        try {
            return tokenType
                    .getConstructor(prependParamType(params))
                    .newInstance(prependParam(parent, sequence, params));
        } catch (NoSuchMethodException |
                 InstantiationException |
                 IllegalAccessException error) {
            throw new TokenInstantiationError(error, tokenType, params);
        } catch (InvocationTargetException error) {
            throw new RuntimeException(error.getCause());
        }
    }

    /**
     * The sequence of tokens that this composite represents.
     */
    protected final Sequence sequence;

    /**
     * Constructs a new CompositeToken with the given sequence of tokens.
     *
     * @param sequence the sequence of tokens
     */
    protected CompositeToken(Sequence sequence) {
        this.sequence = sequence;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRaw() {
        return String.join("", Arrays.stream(this.getContent()).map(IToken::getRaw).toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIndex() {
        return this.getContent()[0].getIndex();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLength() {
        return Arrays.stream(this.getContent()).mapToInt(IToken::getLength).sum();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Sequence getSequence() {
        return this.sequence;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IToken<?>[] getContent() {
        return this.sequence.getContent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return this.sequence.getSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract V getValue();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void parse() throws BaseParseError;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTreeRepr() {
        String children = Arrays.stream(this.getContent())
                .map(IToken::getTreeRepr)
                .reduce("", (a, b) -> a + "\n" + b);
        return "%s%s".formatted(super.getTreeRepr(), children);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSequenceRepr() {
        return "<" + this + ">" + "[" + Arrays.stream(this.getContent()).map(IToken::getSequenceRepr).collect(Collectors.joining(", ")) + "]";
    }

    @Override
    public String toString() {
        if (this.sequence.isDone()) {
            return "%s(%s)@%d".formatted(this.getClass().getSimpleName(), this.getValue(), this.getIndex());
        }
        return "%s(?)@%d".formatted(this.getClass().getSimpleName(), this.getIndex());
    }
}