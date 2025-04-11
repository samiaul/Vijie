package com.vijie.core.interfaces;

/**
 * Interface for tokens.
 * This interface defines the structure and behavior of a token.
 * A token is an element that represents a value from a sequence
 * of characters.
 *
 * @param <V> the type of the value returned by this token
 */
public interface IToken<V> {

    /**
     * Get the raw string of the token.
     *
     * @return The raw string representation of the token.
     */
    String getRaw();

    /**
     * Get the index of the token in the input.
     * If the token is longer than one character,
     * then the index is the start of the token.
     * @return The index of the token.
     */
    int getIndex();

    /**
     * Get the length of the token.
     * @return The length of the token.
     */
    int getLength();

    /**
     * Gets the depth of the token in the parse tree.
     *
     * @return the depth of the token in the parse tree
     */
    int getDepth();

    /**
     * Get the type of the token.
     *
     * @return The class type of the token.
     */
    Class<? extends IToken<V>> getType();

    /**
     * Get the final value of the token.
     *
     * @return The value of the token.
     */
    V getValue();

    /**
     * Compare this token with another.
     * If the other is of the same type, compare their values,
     * otherwise compare their raw representation.
     *
     * @param other The other token to compare with.
     * @return True if the tokens match, false otherwise.
     */
    boolean equals(IToken<?> other);

    /**
     * Compare the value of this token with an object.
     * This method overrides the default equals method to provide
     * a custom comparison logic for token values.
     *
     * @param other The other object to compare with.
     * @return True if this token value equals the other object.
     */
    @Override
    boolean equals(Object other);

    /**
     * Compare the value of this token with a string.
     * This method checks if the string representation of this token
     * equals the provided string.
     *
     * @param value The string to compare with.
     * @return True if this token value equals the provided string.
     */
    boolean equals(String value);

    /**
     * Check if this token matches the specified type.
     *
     * @param other The class type of the other token to compare with.
     * @return True if this token is of the specified type, false otherwise.
     */
    boolean matches(Class<? extends IToken<?>> other);

    /**
     * Check the type of this token against an iterable of other tokens types.
     * This method iterates over the provided list of token types and checks if
     * this token's type matches any of the types in the list.
     *
     * @param others The list of other token types to compare with.
     * @return True if this token's type matches any type in the list, false otherwise.
     */
    boolean matches(Iterable<Class<? extends IToken<?>>> others);

    /**
     * Get the tree representation of the token.
     * This method returns a string that represents the token in a tree structure.
     *
     * @return The tree representation of the token as a string.
     */
    String getTreeRepr();

    String getSequenceRepr();
}
