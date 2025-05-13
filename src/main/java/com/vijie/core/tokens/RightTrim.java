package com.vijie.core.tokens;

import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.interfaces.IToken;
import com.vijie.core.parsers.Factory;

/**
 * Represents a token that trims characters from the right side of a target token.
 * <p>
 * This class extends the Trim class and is used to remove unwanted characters
 * from the right side of a token.
 *
 * @param <V> the type of value returned by the target parser
 * @param <T> the type of token being parsed
 */
public class RightTrim<V, T extends IToken<V>> extends Trim<V, T> {

    /**
     * Creates a factory for RightTrim class with the specified target parser and right blacklist.
     *
     * @param target the target parser
     * @param blacklist the characters to be trimmed from the right
     * @param <V> the type of value returned by the target parser
     * @param <T> the type of token being parsed
     * @return a Factory instance for RightTrim
     */
    public static <V, T extends IToken<V>> Factory<RightTrim<V, T>> parser(Factory<? extends T> target, String blacklist) {
        return Factory.of(RightTrim.class, target, blacklist);
    }

    /**
     * Constructs a new RightTrim instance.
     *
     * @param parent the parent composite
     * @param sequence the sequence of characters
     * @param target the target parser
     * @param blacklist the characters to be trimmed from the right
     */
    public RightTrim(ICompositeToken<?> parent, Sequence sequence, Factory<? extends T> target, String blacklist) {
        super(parent, sequence, target, "", blacklist);
    }

}
