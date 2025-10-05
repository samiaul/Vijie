package com.vijie.core.tokens;


import com.vijie.core.symbols.Atom;
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
        return Factory.of(DefinedChar.class, whitelist);
    }

    /**
     * Creates a factory for DefinedChar with a specified character.
     *
     * @param character the character allowed
     * @return a Factory instance for DefinedChar
     */
    public static Factory<DefinedChar> parser(Character character) {
        return Factory.of(DefinedChar.class, character.toString());
    }

    /**
     * The whitelist of characters allowed.
     */
    protected final String whitelist;

    /**
     * Constructs a DefinedChar node.
     *
     * @param parent    the parent composite node
     * @param sequence  the sequence to parse
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
        return ((Atom) this.getContent()[0]).getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void parse() throws BaseParseError {

        if (this.sequence.isEof()) throw new EOFParseError(this.sequence);

        IToken<?> current = this.sequence.getCurrent();

        if (!current.matches(Atom.class)) {
            throw new ExpectedGlyphError(sequence, current);
        }

        if (!this.whitelist.contains(current.getRaw())) {
            throw new InvalidCharError(sequence, whitelist, (Character) current.getValue());
        }

        ((Atom) current).setParent(this);

        this.sequence.next();
        this.sequence.clearFrom();

    }

    @Override
    public String toString() {
        return "(%s)@%d".formatted(
                escapeJava((this.sequence.isDone()) ? this.getValue().toString() : this.whitelist),
                this.getIndex());
    }
}
