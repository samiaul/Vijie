package com.vijie.core;

import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.INodeToken;
import com.vijie.core.parsers.Char;

import static org.apache.commons.text.StringEscapeUtils.escapeJava;

/**
 * Represents a Glyph, which is a type of Token containing a single character.
 */
public final class Glyph extends Token<Character> implements INodeToken<Character> {

    /**
     * The parser for the Glyph class.
     *
     * @return A new instance of the Char parser.
     */
    public static Char parser() {
        return new Char();
    }

    /**
     * The character value of the Glyph.
     */
    private final Character value;

    /**
     * The index of the Glyph in the text.
     */
    private final int index;

    /**
     * The parent composite node of the Glyph.
     */
    private ICompositeToken<?> parent;

    public Glyph(Character value, int index) {
        this.value = value;
        this.index = index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ICompositeToken<?> getParent() {
        return this.parent;
    }

    /**
     * Set the parent node of this token.
     *
     * @param parent The parent node to set.
     */
    public void setParent(ICompositeToken<?> parent) {
        this.parent = parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRaw() {
        return this.value.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIndex() {
        return this.index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLength() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDepth() {
        if (this.parent == null) return 0;
        return this.parent.getDepth() + 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Character getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "'%s'@%d".formatted(escapeJava(this.value.toString()), index);
    }

    public String getSequenceRepr() {
        return this.toString();
    }
}

