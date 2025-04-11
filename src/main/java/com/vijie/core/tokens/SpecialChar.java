package com.vijie.core.tokens;

import org.apache.commons.text.StringEscapeUtils;
import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.parsers.Factory;

import java.util.Arrays;

import static org.apache.commons.text.StringEscapeUtils.escapeJava;
import static org.apache.commons.text.StringEscapeUtils.unescapeJava;

/**
 * Represents a special character token that can be parsed from a sequence.
 * <p>
 * This class extends the Chain class and provides functionality to parse
 * special characters, including escape sequences.
 */
public class SpecialChar extends Chain<Character, DefinedChar>  {

    /**
     * Creates a factory for SpecialChar class with a specified whitelist.
     *
     * @param whitelist the whitelist of characters allowed
     * @return a Factory instance for DefinedChar
     */
    public static Factory<SpecialChar> parser(String whitelist) {
        return new Factory<>(SpecialChar.class, whitelist);
    }

    /**
     * Constructs a chain of parsers for special characters.
     *
     * @param whitelist the whitelist of characters allowed
     * @return an array of parsers for special characters
     */
    @SuppressWarnings("unchecked")
    private static IParser<DefinedChar>[] constructChain(String whitelist) {
        return new IParser[]{
                DefinedChar.parser("\\"),
                DefinedChar.parser(whitelist)
        };
    }

    /**
     * The whitelist of characters allowed.
     */
    protected final String whitelist;

    /**
     * Constructs a SpecialChar node.
     *
     * @param parent the parent composite node
     * @param sequence the sequence to parse
     * @param whitelist the whitelist of characters allowed
     */
    public SpecialChar(ICompositeToken<?> parent, Sequence sequence, String whitelist) {
        super(parent, sequence, constructChain(whitelist));
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
        return unescapeJava(this.getJoin()).charAt(0);
    }

    /**
     * {@inheritDoc}
     */
    public String getJoin() {
        return Arrays.stream(this.getValues())
                .map(value -> String.valueOf((Character) value))
                .reduce("", String::concat);
    }

    @Override
    public String toString() {
        if (this.sequence.isEof()) return "%s\\@%d".formatted(escapeJava(this.getValue().toString()), this.getIndex());
        return "(\\?\\)@%d".formatted(this.getIndex());
    }

}
