package com.vijie.core.tokens;


import com.vijie.core.NodeToken;
import com.vijie.core.Sequence;
import com.vijie.core.Utils;
import com.vijie.core.errors.*;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IMultiTargetCompositeToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.interfaces.IToken;
import com.vijie.core.parsers.Factory;
import com.vijie.core.parsers.Optional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a chain of optional tokens.
 * <p>
 * This abstract class provides a structure for parsing a sequence of tokens,
 * where each token is optional. At least one token must be present in the sequence.
 *
 * @param <V> the type of the value returned by this token
 * @param <T> the type of the sub-tokens
 */
public abstract class OptionsChain<V, T extends IToken<?>> extends NodeToken<V> implements IMultiTargetCompositeToken<V, T> {

    /** The array of parsers in the chain. */
    protected final IParser<? extends T>[] targets;

    /**
     * Constructs a Chain with the specified parent, sequence, and chain of parsers.
     *
     * @param parent the parent composite
     * @param sequence the sequence to be parsed
     * @param targets the array of parsers in the chain
     */
    protected OptionsChain(ICompositeToken<?> parent, Sequence sequence, IParser<? extends T>[] targets) {
        super(parent, sequence);
        this.targets = targets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IParser<? extends T>[] getTargets() {
        return targets;
    }

    /**
     * Returns the tokens.
     *
     * @return the tokens
     */
    @SuppressWarnings("unchecked")
    public T[] getTokens() {
        return (T[]) this.getContent();
    }

    /**
     * Returns the values of the tokens.
     *
     * @return the values of the tokens
     */
    @SuppressWarnings("unchecked")
    public <W> W[] getValues() {
        return (W[]) Arrays
                .stream(this.getContent())
                .filter(new Utils.filterTrims())
                .map(IToken::getValue)
                .toArray(Object[]::new);
    }

    /**
     * Parses the sequence using the chain of parsers.
     *
     * @throws ParserError if a parsing error occurs
     */
    public void parse() throws BaseParseError {

        List<ParserError> errors = new ArrayList<>();

        for (IParser<? extends T> target : this.targets) {

            try {
                this.sequence.parseAndStep(this, new Optional<>(target));
            } catch (OptionalNotFound error) {
                errors.add(error.getCause());
            } catch (GenericParseError error) {
                //if (error.isEof()) break;
                if (this.sequence.isEof()) break;
                throw error;
            }
        }

        this.sequence.clearFrom();

        if (this.getSize() == 0) {
            throw new EmptyChainError(this.sequence, Factory.getTypes(this.targets), errors.toArray(ParserError[]::new));
        }

    }

}
