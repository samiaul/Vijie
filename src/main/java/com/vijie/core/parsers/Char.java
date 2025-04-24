package com.vijie.core.parsers;

import com.vijie.core.Glyph;
import com.vijie.core.Sequence;
import com.vijie.core.errors.EOFError;
import com.vijie.core.errors.EOFParseError;
import com.vijie.core.errors.ExpectedGlyphError;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.interfaces.IToken;

/**
 * A parser that checks if the current object in the sequence is a Glyph.
 */
public final class Char implements IParser<Glyph> {

    /**
     * Gets the type of object this parser is designed to parse.
     *
     * @return the class type of the object, which is Glyph
     */
    @Override
    public Class<Glyph> getType() {
        return Glyph.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Glyph parse(ICompositeToken<?> parent, Sequence sequence)
            throws ExpectedGlyphError, EOFParseError {

        if (sequence.isEof()) throw new EOFParseError(sequence);

        IToken<?> current = sequence.getCurrent();

        if (current instanceof Glyph glyph) {
            glyph.setParent(parent);
            return glyph;
        }
        throw new ExpectedGlyphError(sequence, current);
    }

    @Override
    public String toString() {
        return "{Char}";
    }
}
