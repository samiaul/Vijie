package com.vijie.core.parsers;

import com.vijie.core.Sequence;
import com.vijie.core.errors.BaseParseError;
import com.vijie.core.errors.OptionalNotFound;
import com.vijie.core.errors.ParserError;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.interfaces.IToken;


/**
 * A parser that attempts to parse a target parser and returns an optional result.
 * If the target parser fails, an OptionalNotFound exception is thrown.
 *
 * @param <T> the type of the token parsed by the target parser
 */
public final class Optional<T extends IToken<?>> implements IParser<T> {

    /**
     * The target parser that this Optional parser will attempt to use.
     */
    private final IParser<? extends T> target;

    /**
     * Constructs an Optional parser with the specified target parser.
     *
     * @param target the target parser
     */
    public Optional(IParser<? extends T> target) {
        this.target = target;
    }

    public static <T extends IToken<?>> Optional<T> of(IParser<T> parser) {
        return new Optional<>(parser);
    }

    /**
     * Returns the target parser.
     *
     * @return the target parser
     */
    public IParser<? extends T> getTarget() {
        return target;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends T> getType() {
        return this.target.getType();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public T parse(ICompositeToken<?> parent, Sequence sequence) throws BaseParseError {
        try {
            return sequence.parse(parent, this.target);
        } catch (ParserError error) {
            throw new OptionalNotFound(sequence, error, this.target);
        }
    }

    @Override
    public String toString() {
        return "{Optional}(%s)".formatted(target);
    }
}
