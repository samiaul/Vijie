package com.vijie.core.tokens;


import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.parsers.Factory;

import java.util.Arrays;

/**
 * Represents an enumeration of strings.
 * <p>
 * This abstract class extends the Enumeration class and accepts only `StringLiteral`.
 */
public abstract class StringEnum extends Enumeration<String, StringLiteral> {

    /**
     * Constructs an array of parsers for the given string literals.
     *
     * @param literals the string literals
     * @return an array of parsers for the string literals
     */
    @SuppressWarnings("unchecked")
    private static Factory<StringLiteral>[] constructTargets(String[] literals) {
        return Arrays.stream(literals).map(StringLiteral::parser).toArray(Factory[]::new);
    }

    private final String[] members;

    /**
     * Constructs a StringEnum with the given parent, sequence, and members.
     *
     * @param parent the parent composite
     * @param sequence the sequence
     * @param members the members of the enumeration
     */
    public StringEnum(ICompositeToken<?> parent, Sequence sequence, String[] members) {
        super(parent, sequence, StringEnum.constructTargets(members));
        this.members = members;
    }

    /**
     * Returns the members of the enumeration.
     *
     * @return the members of the enumeration
     */
    public String[] getMembers() {
        return this.members;
    }

}

