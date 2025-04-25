package com.vijie.core.tokens;


import com.vijie.core.Atom;
import com.vijie.core.Sequence;
import com.vijie.core.errors.*;
import com.vijie.core.interfaces.ICompositeToken;


/**
 * Represents a literal token.
 * <p>
 * This abstract class is used to represent a literal array of characters
 * in the sequence.
 *
 * @param <V> the type of value this literal holds
 */
public abstract class Literal<V> extends CharArray<V, Atom> {

    private static int getLiteralLength(String literal) {
        if (literal.isEmpty()) {
            throw new EmptyLiteralException();
        }
        return literal.length();
    }

    /**
     * The literal string this token represents.
     */
    protected final String literal;

    /**
     * Constructs a new Literal.
     *
     * @param parent the parent composite
     * @param sequence the sequence this literal belongs to
     * @param literal the literal string
     */
    protected Literal(ICompositeToken<?> parent, Sequence sequence, String literal) {
        super(parent, sequence, Atom.parser(), getLiteralLength(literal), literal.length());
        this.literal = literal;
    }

    /**
     * Returns the literal string.
     *
     * @return the literal string
     */
    public String getLiteral() {
        return literal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void parse() throws BaseParseError {

        try {
            super.parse();
        } catch (UndersizedArrayError _) {}

        if (!(this.getJoin().equals(this.literal))) {
            throw new LiteralDoesNotMatch(this.sequence, this.literal, this.getJoin());
        }

    }
}
