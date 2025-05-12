package com.vijie.core.parsers;

import com.vijie.core.Sequence;
import com.vijie.core.errors.*;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A parser that attempts to parse any of the provided parsers.
 *
 * @param <T> the type of the composite token to be parsed
 */
public class Any<T extends ICompositeToken<?>> implements IParser<T> {

    @SafeVarargs
    public static <T extends ICompositeToken<?>> Any<T> of(IParser<? extends T>... targets) {
        return new Any<>(targets);
    }


    /**
     * The array of parsers that this Any parser will attempt to use.
     */
    private final IParser<? extends T>[] targets;

    /**
     * Constructs an Any parser with the given parsers.
     *
     * @param targets the parsers to be used by this Any parser
     */
    @SafeVarargs
    public Any(IParser<? extends T>... targets) {
        this.targets = targets;
    }

    /**
     * Returns the parsers used by this Any parser.
     *
     * @return an array of parsers
     */
    public IParser<? extends T>[] getTargets() {
        return this.targets;
    }


    /**
     * Returns the type of the composite object that this parser can parse.
     * Since this parser has multiple targets and no precise token type,
     * the type is ICompositeToken.
     *
     * @return the class type of the composite object
     */
    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends T> getType() {
        return (Class<? extends T>) (Class<?>) ICompositeToken.class;
    }

    /**
     * Returns the types of the composite objects that can be parsed by the parsers.
     *
     * @return a list of class types of the composite objects
     */
    public List<Class<? extends ICompositeToken<?>>> getTypes() {
        return Factory.getTypes(this.targets);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T parse(ICompositeToken<?> parent, Sequence sequence) throws BaseParseError {

        List<ParserError> errors = new ArrayList<>();

        for (IParser<? extends T> target : this.targets) {
            try {
                sequence.tryParse(parent, target);
                return target.parse(parent, sequence);
            } catch (OptionalNotFound _) {
            } catch (GenericParseError cause) {
                errors.add(new ParserError(sequence, cause, target));
            }
        }

        throw new AnyParserError(this, sequence, errors.toArray(ParserError[]::new));

    }

    @Override
    public String toString() {
        return "{Any}(%s)".formatted(String.join(", ", Arrays.stream(targets).map(IParser::toString).toArray(String[]::new)));
    }
}
