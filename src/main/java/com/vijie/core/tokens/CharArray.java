package com.vijie.core.tokens;

import com.vijie.core.Sequence;
import com.vijie.core.errors.IllegalExtentRangeException;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.interfaces.IToken;

import java.util.Arrays;
import java.util.stream.Collectors;


/**
 * Represents a character array.
 * <p>
 * This abstract class is a specialization of the `Array` class and
 * accepts only character tokens.
 *
 * @param <V> the type of the value
 * @param <T> the type of the token, which extends IToken<Character>
 */
public abstract class CharArray<V, T extends IToken<Character>> extends Array<V, T> {

    /**
     * Constructs a CharArray instance with the specified parameters.
     *
     * @param parent the parent composite that this CharArray belongs to
     * @param sequence the sequence of characters to be parsed
     * @param target the target parser responsible for parsing the tokens
     * @param extentMin the minimum extent of the character array
     * @param extentMax the maximum extent of the character array
     * @throws IllegalExtentRangeException if the extent range is invalid
     */
    protected CharArray(ICompositeToken<?> parent,
                     Sequence sequence,
                     IParser<? extends T> target,
                     int extentMin,
                     int extentMax) throws IllegalExtentRangeException {
        super(parent, sequence, target, extentMin, extentMax);
    }

    /**
     * Constructs a CharArray instance with the specified parameters.
     *
     * @param parent the parent composite that this CharArray belongs to
     * @param sequence the sequence of characters to be parsed
     * @param target the target parser responsible for parsing the tokens
     * @throws IllegalExtentRangeException if the extent range is invalid
     */
    protected CharArray(ICompositeToken<?> parent,
                        Sequence sequence,
                        IParser<? extends T> target) throws IllegalExtentRangeException {
        super(parent, sequence, target);
    }

    /**
     * Joins the character array values into a single string.
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
