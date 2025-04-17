package com.vijie.core;

import com.vijie.core.errors.*;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.interfaces.IToken;
import com.vijie.core.parsers.Optional;
import com.vijie.core.tokens.DefinedChar;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;


/**
 * Represents a sequence of tokens.
 * Implements Iterable to allow iteration over the tokens.
 */
public final class Sequence implements Iterable<IToken<?>> {

    /**
     * Tokenizes the input string into an array of Glyphs.
     *
     * @param input the input string to tokenize
     * @return an array of Glyphs representing the tokenized input
     */
    public static Glyph[] tokenize(String input) {
        return IntStream.range(0, input.length())
                .mapToObj(i -> new Glyph(input.charAt(i), i))
                .toArray(Glyph[]::new);
    }

    /**
     * Creates a Sequence from the input string.
     *
     * @param input the input string
     * @return a new Sequence object
     */
    public static Sequence fromString(String input) {
        return new Sequence(Sequence.tokenize(input));
    }

    /**
     * The list of tokens in the sequence.
     */
    private List<IToken<?>> content;

    /**
     * The current pointer index in the sequence.
     */
    private int pointer;

    /**
     * Constructs a Sequence with the given content and pointer.
     *
     * @param content the array of tokens
     * @param pointer the pointer index
     */
    public Sequence(IToken<?>[] content, int pointer) {
        this.content = new ArrayList<>(Arrays.asList(content));
        this.pointer = pointer;
    }

    /**
     * Constructs a Sequence with the given content and a pointer set to 0.
     *
     * @param content the array of tokens
     */
    public Sequence(IToken<?>[] content) {
        this(content, 0);
    }

    /**
     * Returns the sequence of tokens.
     *
     * @return an array of tokens
     */
    public IToken<?>[] getContent() {
        return content.toArray(new IToken<?>[0]);
    }

    /**
     * Returns the pointer index in the sequence.
     *
     * @return the pointer index
     */
    public int getPointer() {
        return pointer;
    }

    /**
     * Sets the pointer index in the sequence.
     *
     * @param pointer the new pointer index
     */
    public void setPointer(int pointer) {
        this.pointer = pointer;
    }

    /**
     * Returns the number of tokens in the sequence.
     *
     * @return the number of tokens
     */
    public int getSize() {
        return this.content.size();
    }

    /**
     * Returns the number of characters in the sequence.
     *
     * @return the number of characters
     */
    public int getLength() {
        return this.content.stream().mapToInt(IToken::getLength).sum();
    }

    /**
     * Returns a subsequence from the pointer to the end.
     *
     * @return an array of tokens from the pointer to the end
     */
    public IToken<?>[] getRemainder() {
        return this.content.subList(this.pointer, this.getSize()).toArray(new IToken<?>[0]);
    }

    /**
     * Returns a subsequence from the start to the pointer (inclusive).
     *
     * @return an array of tokens from the start to the pointer
     */
    public IToken<?>[] getConsumed() {
        int toIndex = Math.clamp(this.pointer + 1, 0, this.getSize());
        return this.content.subList(0, toIndex).toArray(IToken<?>[]::new);
    }

    /**
     * Returns the current token at the pointer.
     *
     * @return the current token
     * @throws EOFError if the pointer is out of bounds
     */
    public IToken<?> getCurrent() throws EOFError {
        try {
            return this.content.get(this.pointer);
        } catch (IndexOutOfBoundsException e) {
            throw new EOFError(this);
        }
    }

    /**
     * Returns the index of the current token in the sequence.
     * If the pointer is out of bounds, returns the index of the last character.
     *
     * @return the index of the current token or the end index if out of bounds
     */
    public int getCurrentIndex() {
        try {
            return this.getCurrent().getIndex();
        } catch (EOFError e) {
            return this.getEndIndex();
        }
    }

    /**
     * Returns the index of the last character.
     *
     * @return the index of the last character
     */
    public int getEndIndex() {
        if (this.getSize() == 0) return 0;
        return this.content.get(this.getSize() - 1).getIndex() + this.content.get(this.getSize() - 1).getLength();
    }


    /**
     * Returns the pointer index in the sequence.
     * If the current token is a composite token, it retrieves the pointer index
     * from the composite token's sequence. If the pointer is out of bounds, it
     * returns the index of the last character.
     *
     * @return the index of the current token or the end index if out of bounds
     */
    public int getPointerIndex() {
        IToken<?> token;
        try {
            token = this.getCurrent();
        } catch (EOFError e) {
            return this.getEndIndex();
        }
        if (token instanceof ICompositeToken<?>) return ((ICompositeToken<?>) token).getSequence().getPointerIndex();
        return token.getIndex();
    }

    /**
     * Gets the glyph at the specified index.
     *
     * @param index the index of the glyph to retrieve
     * @return the glyph at the specified index
     * @throws IndexOutOfRange if the end of file is reached
     */
    public Glyph getAt(int index) throws IndexOutOfRange {

        for (IToken<?> token : this.content) {

            if (index < token.getIndex() || index >= token.getIndex() + token.getLength()) continue;

            if (token instanceof Glyph glyph) return glyph;
            else if (token instanceof ICompositeToken<?> composite) return composite.getSequence().getAt(index);
            throw new RuntimeException("Unexpected token type: " + token.getClass().getName());

        }

        throw new IndexOutOfRange(index);
    }

    Glyph insert(int index, Character value) {

        Glyph glyph = new Glyph(value, index);

        this.content.add(index, glyph);

        return glyph;

    }

    /**
     * Creates a copy of the sequence with the same pointer.
     *
     * @return a copy of the sequence
     */
    public Sequence copy() {
        return this.copy(this.pointer);
    }

    /**
     * Creates a copy of the sequence with the specified pointer.
     *
     * @param pointer the pointer index for the new sequence
     * @return a copy of the sequence
     */
    public Sequence copy(int pointer) {
        return new Sequence(this.getContent(), pointer);
    }

    /**
     * Creates a copy of the remainder of the sequence from the pointer to the end.
     *
     * @return a copy of the remainder of the sequence
     */
    public Sequence copyRemainder() throws EOFParseError {

        if (this.isEof()) throw new EOFParseError(this);

        return new Sequence(this.getRemainder());
    }

    /**
     * Moves the pointer by the specified number of steps.
     *
     * @param steps the number of steps to move the pointer
     */
    public void move(int steps) {
        this.pointer += steps;
    }

    /**
     * Moves the pointer to the next token.
     */
    public void next() {
        this.move(1);
    }

    /**
     * Checks if the pointer is at the end of the sequence.
     *
     * @return true if the pointer is at the end, false otherwise
     */
    public boolean isEof() {
        return this.pointer >= this.getSize();
    }

    /**
     * Clears the remainder of the sequence from the pointer to the end.
     */
    public void clearRemainder() {
        this.clearFrom(1);
        this.next();
    }

    /**
     * Clears the sequence from the start to the pointer.
     */
    public void clearFrom() {
        this.clearFrom(0);
    }

    /**
     * Clears the sequence from the pointer plus the specified offset to the end.
     *
     * @param offset the offset from the pointer
     */
    public void clearFrom(int offset) {
        this.clearAt(this.pointer + offset);
    }

    /**
     * Removes tokens from the pointer to the end.
     *
     * @param pointer the pointer index to start clearing
     */
    public void clearAt(int pointer) {
        this.slice(0, pointer);
    }

    /**
     * Keeps only the tokens from the start to the specified end index.
     *
     * @param start the start index
     * @param end the end index
     */
    public void slice(int start, int end) {
        int fromIndex = Math.clamp(start, 0, this.getSize());
        int toIndex = Math.clamp(end, fromIndex, this.getSize());
        this.content = new ArrayList<>(this.content.subList(fromIndex, toIndex));
    }

    /**
     * Replaces all tokens from the pointer to pointer + length of the token with the specified token.
     *
     * @param token the token to replace with
     */
    public void fusion(IToken<?> token) {
        this.content.subList(this.pointer, Math.min(this.pointer + token.getLength(), this.getSize())).clear();
        this.content.add(this.pointer, token);
    }

    /**
     * Retrieves the remainder of the sequence for parsing using the specified parser.
     * If the end of the sequence is reached, it throws an EOFParseError.
     * If the parser is an Optional and the end of the sequence is reached, it throws an OptionalNotFound.
     *
     * @param parser the parser to use for retrieving the remainder
     * @return a copy of the remainder of the sequence
     * @throws EOFParseError if the end of the sequence is reached
     * @throws OptionalNotFound if the parser is an Optional and the end of the sequence is reached
     */
    private Sequence getParsingRemainder(IParser<?> parser) throws EOFParseError, OptionalNotFound {
        try {
            return this.copyRemainder();
        } catch (EOFParseError error) {
            if (parser instanceof Optional<?> optional) {
                throw new OptionalNotFound(this, new ParserError(this, error, parser), optional.getTarget());
            }
            throw error;
        }
    }

    /**
     * Parses the input using the specified parser and parent composite token.
     *
     * @param <T> the type of token
     * @param parent the parent composite token
     * @param target the parser to use
     * @return the parsed token
     * @throws GenericParseError if a parsing error occurs
     */
    public <T extends IToken<?>> T find(ICompositeToken<?> parent, IParser<T> target) throws BaseParseError {

        try {
            return target.parse(parent, this.getParsingRemainder(target));
        } catch (OptionalNotFound error) {
            throw error;
        } catch (GenericParseError cause) {
            throw new ParserError(this, cause, target);
        }
    }

    /**
     * Tries to parse and fuse the specified parser, returns the token.
     *
     * @param <T> the type of token
     * @param parent the parent composite token
     * @param target the parser to use
     * @return the parsed token
     * @throws OptionalNotFound if the token is not found
     * @throws ParserError if a parsing error occurs
     */
    public <T extends IToken<?>> T parse(ICompositeToken<?> parent, IParser<T> target) throws BaseParseError {

        T token;

        try {
            token = this.find(parent, target);
        } catch (Interrupter error) {
            this.fusion(error.getToken());
            throw error.getCause();
        }

        this.fusion(token);

        return token;

    }

    /**
     * Parses and fuses the specified parser, then moves the pointer to the next token.
     *
     * @param <T> the type of token
     * @param parent the parent composite token
     * @param target the parser to use
     * @throws GenericParseError if a parsing error occurs
     */
    public <T extends IToken<?>> void parseAndStep(ICompositeToken<?> parent, IParser<T> target) throws BaseParseError {
        this.parse(parent, target);
        this.next();
    }

    /**
     * Parses a character from the input sequence using the specified parent composite token and whitelist.
     *
     * @param parent the parent composite token
     * @param whitelist the string containing allowed characters
     * @throws GenericParseError if a parsing error occurs
     */
    public void parseChar(ICompositeToken<?> parent, String whitelist) throws BaseParseError {
        this.parseAndStep(parent, DefinedChar.parser(whitelist));
    }

    /**
     * Checks if this sequence is equal to another object.
     *
     * @param o the object to compare with
     * @return true if the object is a Sequence and has the same content, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Sequence sequence)) return false;
        return Objects.equals(content, sequence.content);
    }

    /**
     * Computes the hash code for this sequence.
     *
     * @return the hash code of the sequence's content
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(content);
    }

    /**
     * Returns an iterator over the tokens in this sequence.
     *
     * @return an iterator over the tokens
     */
    @Override
    public Iterator<IToken<?>> iterator() {
        return this.content.iterator();
    }

    /**
     * Performs the given action for each token in this sequence.
     *
     * @param action the action to be performed for each token
     */
    @Override
    public void forEach(Consumer<? super IToken<?>> action) {
        Iterable.super.forEach(action);
    }

    /**
     * Creates a Spliterator over the tokens in this sequence.
     *
     * @return a Spliterator over the tokens
     */
    @Override
    public Spliterator<IToken<?>> spliterator() {
        return Iterable.super.spliterator();
    }
}
