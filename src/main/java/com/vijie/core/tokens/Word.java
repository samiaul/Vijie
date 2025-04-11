package com.vijie.core.tokens;


import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.parsers.Factory;


/**
 * Represents a word composed of letters.
 */
public class Word extends CharArray<String, Letter> {

    /**
     * Returns a parser for the Word class.
     *
     * @return a Factory instance for Word
     */
    public static Factory<Word> parser() {
        return new Factory<>(Word.class);
    }

    /**
     * Constructs a Word instance.
     *
     * @param parent the parent composite
     * @param sequence the sequence of characters
     */
    public Word(ICompositeToken<?> parent, Sequence sequence) {
        super(parent, sequence, Letter.parser(), 1, 0);
    }

    /**
     * Returns the value of the word as a string.
     *
     * @return the joined string value of the word
     */
    @Override
    public String getValue() {
        return getJoin();
    }

}

