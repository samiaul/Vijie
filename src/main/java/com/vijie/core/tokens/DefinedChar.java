package com.vijie.core.tokens;


import com.vijie.core.Glyph;
import com.vijie.core.NodeToken;
import com.vijie.core.Sequence;
import com.vijie.core.errors.*;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IToken;
import com.vijie.core.parsers.Factory;

import static org.apache.commons.text.StringEscapeUtils.escapeJava;

/**
 * Represents a specific character token among a set of defined characters.
 */
public class DefinedChar extends NodeToken<Character> {

    /**
     * Creates a factory for DefinedChar with a specified whitelist.
     *
     * @param whitelist the whitelist of characters allowed
     * @return a Factory instance for DefinedChar
     */
    public static Factory<DefinedChar> parser(String whitelist) {
        return new Factory<>(DefinedChar.class, whitelist);
    }

    /**
     * The whitelist of characters allowed.
     */
    private final String whitelist;

    /**
     * Constructs a DefinedChar node.
     *
     * @param parent the parent composite node
     * @param sequence the sequence to parse
     * @param whitelist the whitelist of characters allowed
     */
    public DefinedChar(ICompositeToken<?> parent, Sequence sequence, String whitelist) {
        super(parent, sequence);
        this.whitelist = whitelist;
    }

    /**
     * Gets the whitelist of characters.
     *
     * @return the whitelist of characters
     */
    public String getWhitelist() {
        return this.whitelist;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Character getValue() {
        return ((Glyph) this.getContent()[0]).getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void parse() throws BaseParseError {

        IToken<?> current;
        try {
            current = this.sequence.getCurrent();
        } catch (EOFError e) {
            throw new EOFParseError(this.sequence);
        }

        if (!current.matches(Glyph.class)) {
            throw new ExpectedGlyphError(sequence, current);
        }

        if (!this.whitelist.contains(current.getRaw())) {
            throw new InvalidCharError(sequence, whitelist, (Character) current.getValue());
        }

        ((Glyph) current).setParent(this);

        this.sequence.next();
        this.sequence.clearFrom();

    }

    @Override
    public String toString() {
        try {
            if (this.sequence.isEof()) return "(%s)@%d".formatted(escapeJava(this.getValue().toString()), this.getIndex());
            return "(?)@%d".formatted(this.getIndex());
        } catch (Exception e) {
            return "DefinedChar(\"%s\")".formatted(this.getWhitelist());
        }
    }
}
