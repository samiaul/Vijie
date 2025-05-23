package com.vijie.core;

import com.vijie.core.errors.*;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.interfaces.ISymbol;
import com.vijie.core.interfaces.IToken;
import com.vijie.core.parsers.Char;
import com.vijie.core.symbols.Atom;
import com.vijie.core.symbols.EOF;
import com.vijie.core.symbols.Symbol;
import com.vijie.core.tokens.DefinedChar;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;


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
    public static Token<?>[] tokenize(String input) {
        return Stream.concat(
                IntStream.range(0, input.length())
                        .mapToObj(i -> new Atom(input.charAt(i), i)),
                Stream.of(new EOF(input.length()))
        ).toArray(Token<?>[]::new);
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
     * Returns the sequence of tokens as a stream.
     *
     * @return a stream of tokens
     */
    public Stream<IToken<?>> stream() {
        return this.content.stream();
    }

    /**
     * Returns the sequence of tokens as a TokenStream.
     *
     * @return a TokenStream of tokens
     */
    public TokenStream<?, IToken<?>> tokenStream() {
        return new TokenStream(this.content);
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
     * @throws EOFException if the pointer is out of bounds
     */
    public IToken<?> getCurrent() throws EOFException {
        try {
            return this.content.get(this.pointer);
        } catch (IndexOutOfBoundsException e) {
            throw new EOFException(this);
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
        } catch (EOFException e) {
            return this.getEndIndex();
        }
    }

    /**
     * Returns the index of the first character.
     *
     * @return the index of the first character
     */
    public int getStartIndex() {
        if (this.getSize() == 0) throw new EOFException(this);
        return this.content.getFirst().getIndex();
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
        } catch (EOFException e) {
            return this.getEndIndex();
        }
        if (token instanceof ICompositeToken<?>) return ((ICompositeToken<?>) token).getSequence().getPointerIndex();
        return token.getIndex();
    }

    /**
     * Returns the token at the specified cursor.
     *
     * @param cursor the index in the sequence of the token to retrieve
     * @return the token at the specified index
     */
    @SuppressWarnings("unchecked")
    public <T extends IToken<?>> T get(int cursor) {
        return (T) this.content.get(cursor);
    }

    /**
     * Gets the Atom at the specified index.
     *
     * @param index the index of the Atom to retrieve
     * @return the Atom at the specified index
     * @throws IndexOutOfRange if the end of file is reached
     */
    public Symbol<?> getSymbol(int index) throws IndexOutOfRange {

        IToken<?> token = this.getAt(index);

        return switch (token) {
            case Symbol<?> symbol -> symbol;
            case ICompositeToken<?> composite -> composite.getSequence().getSymbol(index);
            default -> throw new IndexOutOfRange(index);
        };


    }

    /**
     * Gets the token at the specified index.
     *
     * @param index the index of the token to retrieve
     * @return the token at the specified index
     * @throws IndexOutOfRange if the end of file is reached
     */
    public IToken<?> getAt(int index) throws IndexOutOfRange {

        for (IToken<?> token : this.content) {
            if (index >= token.getIndex() && index < token.getIndex() + token.getLength()) return token;
        }

        throw new IndexOutOfRange(index);
    }


    Atom insert(int index, Character value) {

        Atom atom = new Atom(value, index);

        this.content.add(index, atom);

        return atom;

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
    public Sequence copyRemainder() throws EOFException, EOFParseError {

        if (this.isDone()) throw new EOFException(this);

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
     * Checks if the current token is EOF token.
     *
     * @return true if the current token is EOF token, false otherwise
     */
    public boolean isEof() {
        try {
            return this.getCurrent().matches(EOF.class);
        } catch (EOFException e) {
            return true;
        }
    }

    /**
     * Checks if the pointer is at or after the end of the sequence.
     *
     * @return true if the pointer is at or after the end, false otherwise
     */
    public boolean isDone() {
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
     * Clears the sequence from the pointer to the end.
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
        this.reroot(token);
    }

    private void reroot(IToken<?> token) {

        if (token instanceof ICompositeToken<?> parent) {
            for (IToken<?> child : parent.getContent()) {
                if (child instanceof ISymbol<?> symbol) symbol.setParent(parent);
                else if (child instanceof ICompositeToken<?> composite) composite.getSequence().reroot(composite);
            }
        }

    }

    public void append(IToken<?> token) {
        if (token.getLength() != 0) throw new RuntimeException("Token length must be 0");
        this.content.add(token);
    }

    public void find(ICompositeToken<?> parent, IParser<?> target) throws NotFoundError {

        while (!this.isEof()) {

            try {
                this.tryParse(parent, target);
            } catch (BaseParseError | Interruption _) {
                try {
                    this.parseAndStep(parent, new Char());
                } catch (BaseParseError _) {
                    break;
                }
                continue;
            }

            return;
        }

        throw new NotFoundError(target);

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
    public <T extends IToken<?>> T tryParse(ICompositeToken<?> parent, IParser<T> target) throws BaseParseError {

        GenericParseError error;

        try {
            return target.parse(parent, this.copyRemainder());
        } catch (OptionalNotFound err) {
            throw err;
        } catch (GenericParseError cause) {
            error = (this.isEof())?new EOFParseError(this):cause;
        }

        throw new ParserError(this, error, target);
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
            token = this.tryParse(parent, target);
        } catch (FailedTokenInterruption interruption) {
            token = this.onFail(parent, target, interruption);
        } catch (Interruption interruption) {
            this.fusion(interruption.getToken());
            this.clearRemainder();
            throw interruption.getCause();
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

    private <T extends IToken<?>> T onFail(ICompositeToken<?> parent, IParser<T> target, FailedTokenInterruption interruption) throws BaseParseError {

        if (interruption.getError() instanceof UnexpectedTokenError) {
            this.fusion(interruption.getToken());
            this.next();
            return this.tryParse(parent, target);
        }

        return interruption.getToken();

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

    public String getRaw() {
        return String.join("", this.content.stream().map(IToken::getRaw).toList());
    }

}
