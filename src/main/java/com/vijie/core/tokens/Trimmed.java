package com.vijie.core.tokens;

import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IToken;
import com.vijie.core.parsers.Factory;
import com.vijie.core.parsers.Optional;

import javax.lang.model.type.NullType;

/**
 * Represents a trimmed token that excludes specified characters.
 * <p>
 * This class as a null value and is ignored by other tokens.
 */
public class Trimmed extends CharArray<NullType, IToken<Character>>{

    /**
     * Creates a parser for the Trimmed class with the specified blacklist.
     *
     * @param blacklist the characters to be excluded from the parsing
     * @return a Factory instance for Trimmed
     */
    public static Optional<Trimmed> parser(String blacklist) {
        return Optional.of(Factory.of(Trimmed.class, blacklist));
    }

    /**
     * Constructs a new Trimmed token with the specified parent, sequence, and blacklist.
     *
     * @param parent the parent composite
     * @param sequence the sequence of characters
     * @param blacklist the characters to be excluded from the parsing
     */
    public Trimmed(ICompositeToken<?> parent, Sequence sequence, String blacklist) {
        super(parent, sequence, DefinedChar.parser(blacklist), 1, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NullType getValue() {
        return null;
    }

    @Override
    public String toString() {
        return "Trimmed@%d".formatted(this.getIndex());
    }

}
