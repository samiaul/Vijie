package com.vijie.core.tokens;

import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.interfaces.IToken;
import com.vijie.core.parsers.Factory;

/**
 * Represents a token that trims characters from the left side of a target token.
 * <p>
 * This class extends the Trim class and is used to remove unwanted characters
 * from the left side of a token.
 *
 * @param <V> the type of value returned by the target parser
 * @param <T> the type of token being parsed
 */
public class LeftTrim<V, T extends IToken<V>> extends Trim<V, T> {

    /**
     * Creates a factory for LeftTrim class with the specified target parser and left blacklist.
     *
     * @param target the target parser
     * @param blacklist the characters to be trimmed from the left
     * @param <V> the type of value returned by the target parser
     * @param <T> the type of token being parsed
     * @return a Factory instance for LeftTrim
     */
    @SuppressWarnings("unchecked")
    public static <V, T extends IToken<V>> Factory<LeftTrim<V, T>> parser(Factory<? extends T> target, String blacklist) {
        return new Factory<>((Class<LeftTrim<V, T>>) (Class<?>) LeftTrim.class, target, blacklist);
    }

    /**
     * Constructs a new LeftTrim instance.
     *
     * @param parent the parent composite
     * @param sequence the sequence of characters
     * @param target the target parser
     * @param blacklist the characters to be trimmed from the left
     */
    public LeftTrim(ICompositeToken<?> parent, Sequence sequence, Factory<? extends T> target, String blacklist) {
        super(parent, sequence, target, blacklist, "");
    }

}
