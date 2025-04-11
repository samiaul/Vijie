package com.vijie.core.parsers;

import com.vijie.core.tokens.StringLiteral;

import static com.vijie.core.Utils.mergeLiterals;
import static com.vijie.core.Utils.mergeStrings;

/**
 * The AnyString class extends the Any class with a type parameter of StringLiteral.
 * It represents a collection of string parsers.
 */
public final class AnyString extends Any<StringLiteral> {

    /**
     * An array of string literals.
     */
    private final String[] literals;

    /**
     * Constructs an AnyString object by merging the provided strings.
     *
     * @param first the first string.
     * @param second the second string.
     * @param others additional strings.
     */
    public AnyString(String first, String second, String... others) {
        super(mergeLiterals(mergeStrings(first, second, others)));
        this.literals = mergeStrings(first, second, others);
    }

    /**
     * Returns the array of string literals.
     *
     * @return an array of string literals.
     */
    public String[] getLiterals() {
        return literals;
    }

    @Override
    public String toString() {
        return "{Literals}(%s)".formatted(String.join(", ", this.literals));
    }
}

