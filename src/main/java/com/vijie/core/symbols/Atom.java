package com.vijie.core.symbols;

import com.vijie.core.Token;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.INodeToken;
import com.vijie.core.interfaces.IToken;
import com.vijie.core.parsers.Char;

import static org.apache.commons.text.StringEscapeUtils.escapeJava;

/**
 * Represents a Atom, which is a type of Token containing a single character.
 */
public final class Atom extends Symbol<Character> {

    /**
     * The parser for the Atom class.
     *
     * @return A new instance of the Char parser.
     */
    public static Char parser() {
        return new Char();
    }

    /**
     * The parent composite node of the Atom.
     */
    private ICompositeToken<?> parent;

    public Atom(Character value, int index) {
        super(value, index, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRaw() {
        return this.value.toString();
    }

    @Override
    public String toString() {
        return "'%s'@%d".formatted(escapeJava(this.value.toString()), index);
    }

}

