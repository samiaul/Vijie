package com.vijie.core.tokens;


import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.interfaces.IToken;
import com.vijie.core.parsers.Factory;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Represents an array of strings tokens.
 * <p>
 * This abstract class is a specialization of the `Array` class and
 * accepts only string tokens.
 *
 * @param <T> the type of elements in the array, extending ICompositeToken<String>
 */
public abstract class StringArray<T extends IToken<String>> extends Array<String, T> {

    /**
     * Constructs a new StringArray.
     *
     * @param parent the parent composite
     * @param sequence the sequence
     * @param target the target parser
     */
    protected StringArray(ICompositeToken<?> parent,
                       Sequence sequence,
                       IParser<? extends T> target,
                       int extentMin,
                       int extentMax) {
        super(parent, sequence, target, extentMin, extentMax);
    }

    /**
     * Joins the string array values into a single string.
     *
     * @return the joined string
     */
    public String getJoin() {
        return Arrays
                .stream(this.getValues())
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

}

