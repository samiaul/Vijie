package com.vijie.core.tokens;


import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.parsers.Factory;

/**
 * Represents a Letter.
 * <p>
 * This class extends the Union class and handles both Uppercase and Lowercase letters.
 */
public final class Letter extends Union<Character, ICompositeToken<Character>> {

    /**
     * Creates a factory for the Letter class.
     *
     * @return a Factory instance for Letter
     */
    public static Factory<Letter> parser() {
        return new Factory<>(Letter.class);
    }

    /**
     * Constructs the target parsers for Uppercase and Lowercase letters.
     *
     * @return an array of Parsers for ICompositeToken<Character>
     */
    @SuppressWarnings("unchecked")
    private static Factory<? extends ICompositeToken<Character>>[] constructTargets() {
        return new Factory[]{ Uppercase.parser(), Lowercase.parser() };
    }

    /**
     * Constructs a new Letter instance.
     *
     * @param parent the parent composite
     * @param sequence the sequence associated with this Letter
     */
    public Letter(ICompositeToken<?> parent, Sequence sequence) {
        super(parent, sequence, Letter.constructTargets());
    }

}
