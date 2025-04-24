package com.vijie.core.errors;

import com.vijie.core.Sequence;

import static org.apache.commons.text.StringEscapeUtils.escapeJava;

/**
 * This class represents an error that occurs when a literal does not match the expected value.
 */
public final class LiteralDoesNotMatch extends GenericParseError {

    private final String literal;
    private final String value;

    /**
     * Constructs a new LiteralDoesNotMatch error.
     *
     * @param sequence The sequence in which the error occurred.
     * @param literal  The expected literal value.
     * @param value    The actual value that was parsed.
     */
    public LiteralDoesNotMatch(Sequence sequence, String literal, String value) {
        super(sequence, String.format("Content does not match literal '%s': '%s'", literal, escapeJava((value.length() <= 15)?value:(value.substring(0, 15) + "..."))));
        this.literal = literal;
        this.value = escapeJava((value.length() <= 15)?value:(value.substring(0, 15) + "..."));
    }

    /**
     * Gets the expected literal value.
     *
     * @return The expected literal value.
     */
    public String getLiteral() {
        return this.literal;
    }

    /**
     * Gets the actual value that was parsed.
     *
     * @return The actual value that was parsed.
     */
    public String getValue() {
        return this.value;
    }

}
