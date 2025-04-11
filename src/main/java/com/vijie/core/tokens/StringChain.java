package com.vijie.core.tokens;


import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.interfaces.IToken;

import java.util.Arrays;
import java.util.stream.Collectors;


/**
 * Represents a chain of string tokens.
 * <p>
 * This abstract class is a specialization of the `Chain` class and
 * accepts only string tokens.
 *
 * @param <T> the type of token that extends IToken<String>
 */
public abstract class StringChain<T extends IToken<String>> extends Chain<String, T> {

    /**
     * Constructs a new StringChain.
     *
     * @param parent the parent composite
     * @param sequence the sequence of tokens
     * @param chain the array of IParser objects
     */
    public StringChain(ICompositeToken<?> parent, Sequence sequence, IParser<? extends T>[] chain) {
        super(parent, sequence, chain);
    }

    /**
     * Joins the string chain values into a single string.
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
