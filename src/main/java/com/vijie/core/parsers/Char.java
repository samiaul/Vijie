package com.vijie.core.parsers;

import com.vijie.core.symbols.Atom;
import com.vijie.core.Sequence;
import com.vijie.core.errors.EOFParseError;
import com.vijie.core.errors.ExpectedGlyphError;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.interfaces.IToken;

/**
 * A parser that checks if the current object in the sequence is a Atom.
 */
public final class Char implements IParser<Atom> {

    /**
     * Gets the type of object this parser is designed to parse.
     *
     * @return the class type of the object, which is Atom
     */
    @Override
    public Class<Atom> getType() {
        return Atom.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Atom parse(ICompositeToken<?> parent, Sequence sequence)
            throws ExpectedGlyphError, EOFParseError {

        if (sequence.isEof()) throw new EOFParseError(sequence);

        IToken<?> current = sequence.getCurrent();

        if (current instanceof Atom atom) {
            atom.setParent(parent);
            return atom;
        }

        throw new ExpectedGlyphError(sequence, current);
    }

    @Override
    public String toString() {
        return "{Char}";
    }
}
