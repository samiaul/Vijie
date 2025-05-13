package com.vijie.core.tokens;


import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.parsers.Factory;


/**
 * Represents a string literal token.
 */
public class StringLiteral extends Literal<String> {

    /**
     * Creates a factory for a StringLiteral class with the given string literal.
     *
     * @param literal the string literal to parse
     * @return a Factory instance for StringLiteral
     */
    public static Factory<StringLiteral> parser(String literal) {
        return Factory.of(StringLiteral.class, literal);
    }

    /**
     * Constructs a new StringLiteral.
     *
     * @param parent the parent composite
     * @param sequence the sequence of tokens
     * @param literal the string literal value
     */
    public StringLiteral(ICompositeToken<?> parent, Sequence sequence, String literal) {
        super(parent, sequence, literal);
    }

    /**
     * Returns the value of the string literal.
     *
     * @return the string literal value
     */
    @Override
    public String getValue() {
        return this.literal;
    }

}
