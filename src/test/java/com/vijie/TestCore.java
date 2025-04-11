package com.vijie;

import org.junit.jupiter.api.Test;
import com.vijie.core.Glyph;
import com.vijie.core.Sequence;
import com.vijie.core.errors.*;
import com.vijie.core.interfaces.IToken;
import com.vijie.core.parsers.Char;
import com.vijie.core.parsers.Factory;
import com.vijie.core.parsers.Optional;
import com.vijie.core.tokens.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


public class TestCore {

    @Test
    void testGlyph() {

        Glyph glyph = new Glyph('a', 0);

        assertEquals('a', glyph.getValue());
        assertEquals("a", glyph.getRaw());
        assertEquals(0, glyph.getIndex());
        assertEquals(1, glyph.getLength());

    }

    @Test
    void testTokenization() {
        Glyph[] sequence = Sequence.tokenize("aB1!");

        assertEquals('a', sequence[0].getValue());
        assertEquals('B', sequence[1].getValue());
        assertEquals('1', sequence[2].getValue());
        assertEquals('!', sequence[3].getValue());

        assertEquals(0, sequence[0].getIndex());
        assertEquals(1, sequence[1].getIndex());
        assertEquals(2, sequence[2].getIndex());
        assertEquals(3, sequence[3].getIndex());

    }

    @Test
    void testContext() throws EOFError {

        Glyph[] content = Sequence.tokenize("ABCDEF");

        Sequence sequence = new Sequence(content);

        assertArrayEquals(content, sequence.getContent());
        assertEquals(6, sequence.getSize());
        assertEquals(6, sequence.getLength());
        assertEquals(0, sequence.getPointer());
        assertEquals(0, sequence.getPointerIndex());
        assertEquals(0, sequence.getCurrentIndex());
        assertEquals(6, sequence.getEndIndex());

        assertSame(sequence.getContent()[0], sequence.getCurrent());
        assertSame(content[0], sequence.getCurrent());

        Sequence context2 = new Sequence(content, 1);
        assertEquals(1, context2.getPointer());

    }

    @Test
    void testContextMovement() throws EOFError {

        Sequence sequence = Sequence.fromString("ABC");

        sequence.move(1);
        assertEquals(1, sequence.getPointer());
        assertEquals('B', sequence.getCurrent().getValue());

        sequence.next();
        assertEquals(2, sequence.getPointer());
        assertEquals('C', sequence.getCurrent().getValue());

        sequence.setPointer(0);
        assertEquals(0, sequence.getPointer());
        assertEquals('A', sequence.getCurrent().getValue());

        sequence.setPointer(3);
        assertTrue(sequence.isEof());

    }

    @Test
    void testContextSubsequence() {
        Glyph[] content = Sequence.tokenize("01234");
        Sequence sequence = new Sequence(content);

        sequence.setPointer(2);

        assertArrayEquals(Arrays.copyOfRange(content, 0, 3), sequence.getConsumed());
        assertArrayEquals(Arrays.copyOfRange(content, 2, 5), sequence.getRemainder());

    }

    @Test
    void testContextCopy() {
        Sequence sequence = Sequence.fromString("ABCDEF");

        Sequence copy = sequence.copy();

        assertArrayEquals(sequence.getContent(), copy.getContent());
        assertEquals(sequence.getPointer(), copy.getPointer());

        sequence.setPointer(1);

        assertEquals(sequence.getPointer(), sequence.copy().getPointer());
        assertEquals(sequence.getPointer(), sequence.copy(1).getPointer());

        assertDoesNotThrow(() -> assertArrayEquals(sequence.getRemainder(), sequence.copyRemainder().getContent()));

    }

    @Test
    void testContextClear() {
        Glyph[] content = Sequence.tokenize("ABCDEF");

        Sequence sequence = new Sequence(content);
        sequence.clearRemainder();
        assertEquals(1, sequence.getSize());

        Sequence context2 = new Sequence(content, 1);
        context2.clearFrom();
        assertEquals(1, context2.getSize());

        Sequence context3 = new Sequence(content);
        context3.clearFrom(1);
        assertEquals(1, context3.getSize());

        Sequence context4 = new Sequence(content);
        context4.clearAt(1);
        assertEquals(1, context4.getSize());
    }

    @Test
    void testParser() {

        assertEquals(DefinedChar.class, DefinedChar.parser("A").getType());

    }

    @Test
    void testDefinedChar() {

        RootParser<Character, DefinedChar> root = new RootParser<>("A", DefinedChar.parser("A"));

        assertDoesNotThrow(root::parse);

        assertEquals("A", root.getToken().getWhitelist());
        assertEquals('A', root.getValue());
        assertEquals("A", root.getToken().getRaw());
        assertEquals(0, root.getToken().getIndex());
        assertEquals(1, root.getToken().getSize());
        assertEquals(1, root.getToken().getLength());
        assertEquals(root.getSequence(), root.getToken().getSequence());
        assertEquals(DefinedChar.class, root.getToken().getType());
        assertArrayEquals(root.getContent(), root.getToken().getContent());
        assertEquals("(A)@0", root.getToken().toString());

        DefinedChar token = new DefinedChar(new DummyRoot(), Sequence.fromString("A"), "A");
        assertEquals("(?)@0", token.toString());
    }

    @Test
    void testDefinedCharEof() {

        DefinedChar token = new DefinedChar(new DummyRoot(), Sequence.fromString(""), "A");

        assertEquals("DefinedChar(\"A\")", token.toString());

        assertThrows(EOFParseError.class, token::parse);
    }

    @Test
    void testDefinedCharWhitelist(){

        RootParser<Character, DefinedChar> root = new RootParser<>(":", DefinedChar.parser("!:;"));

        assertDoesNotThrow(root::parse);

        assertEquals("!:;", root.getToken().getWhitelist());
        assertEquals(':', root.getValue());
        assertEquals(":", root.getToken().getRaw());
    }

    @Test
    void testInvalidChar() {

        RootParser<Character, DefinedChar> root = new RootParser<>("1", DefinedChar.parser("ABC"), false);

        InvalidCharError error = assertThrows(InvalidCharError.class, root::parse);

        assertEquals("ABC", error.getExpected());
        assertEquals('1', error.getActual());
    }

    @Test
    void testExpectedChar() {

        RootParser<Character, DefinedChar> inner = new RootParser<>("A", DefinedChar.parser("ABC"));

        assertDoesNotThrow(inner::parse);

        IToken<?>[] sequence = new IToken[]{inner.getToken()};

        RootParser<Character, DefinedChar> root = new RootParser<>(sequence, DefinedChar.parser("ABC"), false);

        ExpectedGlyphError error = assertThrows(ExpectedGlyphError.class, root::parse);

        assertEquals(sequence[0], error.getToken());
    }

    @Test
    void testTokenEquality() {

        Sequence sequence = Sequence.fromString("AA");

        RootParser<Character, Glyph> root = new RootParser<>("", Glyph.parser());

        DefinedChar token1 = new DefinedChar(root, sequence.copy(), "A");
        DefinedChar token2 = new DefinedChar(root, sequence.copy(1), "A");

        assertEquals(token1, token2);
    }

    @Test
    void testLowercase() {

        RootParser<Character, Lowercase> root = new RootParser<>("a", Lowercase.parser());

        assertDoesNotThrow(root::parse);

        assertEquals('a', root.getValue());
    }

    @Test
    void testUppercase() {

        RootParser<Character, Uppercase> root = new RootParser<>("A", Uppercase.parser());

        assertDoesNotThrow(root::parse);

        assertEquals('A', root.getValue());
    }

    @Test
    void TestEmptyArrayError() {

        RootParser<String, Numeric> root = new RootParser<>("ABC", Numeric.parser(), false);

        assertThrows(UndersizedArrayError.class, root::parse);
    }

    @Test
    void testUnion() {

        Factory<DefinedChar>[] targets = new Factory[]{DefinedChar.parser("1"), DefinedChar.parser("2")};
        Factory<DummyUnion> parser1 = DummyUnion.parser(targets);
        Factory<DefinedChar> parser2 = DefinedChar.parser("1");

        RootParser<Character, DummyUnion> root1 = new RootParser<>("1", parser1);
        RootParser<Character, DefinedChar> root2 = new RootParser<>("1", parser2);

        assertDoesNotThrow(root1::parse);
        assertDoesNotThrow(root2::parse);

        assertEquals('1', root1.getToken().getValue());
        assertEquals(root1.getToken().getToken(), root2.getToken());
        assertEquals(targets, root1.getToken().getTargets());

    }

    @Test
    void testUnionError() {

        Factory<DefinedChar>[] targets = new Factory[]{DefinedChar.parser("1"), DefinedChar.parser("2")};
        Factory<DummyUnion> parser = DummyUnion.parser(targets);

        RootParser<Character, DummyUnion> root = new RootParser<>("A", parser, false);

        assertThrows(MultiParseError.class, root::parse);

    }

    @Test
    void testLetter() {

        RootParser<Character, Letter> root1 = new RootParser<>("A", Letter.parser());
        RootParser<Character, Letter> root2 = new RootParser<>("z", Letter.parser());
        RootParser<Character, Letter> root3 = new RootParser<>("1", Letter.parser(), false);

        assertDoesNotThrow(root1::parse);
        assertDoesNotThrow(root2::parse);
        assertThrows(MultiParseError.class, root3::parse);

        assertEquals('A', root1.getValue());
        assertEquals('z', root2.getValue());

    }

    @Test
    void testWord() {

        RootParser<String, Word> root = new RootParser<>("Hello", Word.parser());

        assertDoesNotThrow(root::parse);

        assertEquals("Hello", root.getValue());
        assertEquals(5, root.getToken().getLength());
        assertEquals(5, root.getToken().getSize());

    }

    @Test
    void testDigit() {

        RootParser<Character, Digit> root = new RootParser<>("1", Digit.parser());

        assertDoesNotThrow(root::parse);

        assertEquals('1', root.getValue());

    }

    @Test
    void testNonDigit() {

        RootParser<Character, Digit> root = new RootParser<>("A", Digit.parser());

        InvalidCharError error = assertThrows(InvalidCharError.class, root::parse);

        assertEquals("0123456789", error.getExpected());
        assertEquals('A', error.getActual());
    }

    @Test
    void TestNumeric() {

        RootParser<String, Numeric> root1 = new RootParser<>("1", Numeric.parser());
        RootParser<String, Numeric> root2 = new RootParser<>("123", Numeric.parser());
        RootParser<String, Numeric> root3 = new RootParser<>("123A", Numeric.parser());

        assertDoesNotThrow(root1::parse);
        assertDoesNotThrow(root2::parse);
        assertDoesNotThrow(root3::parse);

        assertEquals("1", root1.getValue());
        assertEquals("123", root2.getValue());
        assertEquals("123", root3.getValue());

        assertEquals(1, root1.getToken().getLength());
        assertEquals(3, root2.getToken().getLength());
        assertEquals(3, root3.getToken().getLength());

        assertEquals(1, root1.getToken().getSize());
        assertEquals(3, root2.getToken().getSize());
        assertEquals(3, root3.getToken().getSize());

    }

    @Test
    void TestStringLiteral() {

        RootParser<String, StringLiteral> root = new RootParser<>("foo", StringLiteral.parser("foo"));

        assertDoesNotThrow(root::parse);

        assertEquals("foo", root.getValue());
        assertEquals("foo", root.getToken().getLiteral());
        assertEquals(3, root.getToken().getLength());
        assertEquals(3, root.getToken().getSize());
        assertInstanceOf(Char.class, root.getToken().getTarget());

    }

    @Test
    void TestStringEnum() {

        RootParser<String, StringEnum> root = new RootParser<>("bar", StringEnum.parser("foo", "bar", "zip"));

        assertDoesNotThrow(root::parse);

        assertEquals("bar", root.getValue());
        assertEquals(3, root.getToken().getLength());
        assertEquals(1, root.getToken().getSize());
        assertArrayEquals(new String[]{"foo", "bar", "zip"}, root.getToken().getMembers());

    }

    @Test
    void TestStringEnumError() {

        RootParser<String, StringEnum> root = new RootParser<>("zi", StringEnum.parser("foo", "bar", "zip"), false);

        assertThrows(MultiParseError.class, root::parse);

    }

    @Test
    void testChar() {

        RootParser<Character, DefinedChar> inner = new RootParser<>("A", DefinedChar.parser("A"));

        assertDoesNotThrow(inner::parse);

        RootParser<Character, Glyph> root1 = new RootParser<>("A", new Char());
        RootParser<Character, Glyph> root2 = new RootParser<>(new IToken[]{inner.getToken()}, new Char(), false);

        assertDoesNotThrow(root1::parse);

        assertEquals(Glyph.class, root1.getToken().getType());
        assertEquals(new Glyph('A', 0), root1.getToken());

        assertThrows(ExpectedGlyphError.class, root2::parse);

    }

    @Test
    void testOptional() {

        Factory<DefinedChar> target = DefinedChar.parser("1");
        Optional<DefinedChar> parser = new Optional<>(target);

        assertEquals(target, parser.getTarget());
        assertEquals(DefinedChar.class, parser.getType());

        RootParser<Character, DefinedChar> root1 = new RootParser<>("1", parser);
        RootParser<Character, DefinedChar> root2 = new RootParser<>("A", parser, false);

        assertDoesNotThrow(root1::parse);

        assertEquals("1", root1.getToken().getRaw());

        assertThrows(OptionalNotFound.class, root2::parse);

    }

    @Test
    void testCast() {

        Factory<DummyCast> parser = DummyCast.parser();

        RootParser<Integer, DummyCast> root1 = new RootParser<>("1", parser);

        assertDoesNotThrow(root1::parse);

        assertEquals(1, root1.getValue());

    }

    @Test
    void testStringLiteral() {

        RootParser<String, StringLiteral> root = new RootParser<>("hello1", StringLiteral.parser("hello"));

        assertDoesNotThrow(root::parse);

        assertEquals("hello", root.getToken().getRaw());
        assertEquals("hello", root.getValue());
    }

    @Test
    void testEmptyLiteral() throws BaseParseError {

        Factory<StringLiteral> parser = StringLiteral.parser("");
        RootParser<String, StringLiteral> root = new RootParser<>("A", parser);

        RuntimeException error = assertThrows(RuntimeException.class, root::parse);

        assertEquals(EmptyLiteralException.class, error.getCause().getClass());
    }

    @Test
    void testSpecialChar() {

        RootParser<Character, SpecialChar> root = new RootParser<>("\\n", SpecialChar.parser("tn"));

        assertDoesNotThrow(root::parse);

        assertEquals("\\n", root.getToken().getRaw());
        assertEquals("\\n", root.getToken().getJoin());
        assertArrayEquals(new Character[] {'\\', 'n'}, root.getToken().getValues());
        assertEquals('\n', root.getValue());

    }

}

