package com.vijie;

import com.vijie.core.symbols.Atom;
import com.vijie.core.Token;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.INodeToken;
import org.junit.jupiter.api.Test;
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

        Atom atom = new Atom('a', 0);

        assertEquals('a', atom.getValue());
        assertEquals("a", atom.getRaw());
        assertEquals(0, atom.getIndex());
        assertEquals(1, atom.getLength());

    }

    @Test
    void testTokenization() {
        Token<?>[] sequence = Sequence.tokenize("aB1!");

        assertEquals('a', sequence[0].getValue());
        assertEquals('B', sequence[1].getValue());
        assertEquals('1', sequence[2].getValue());
        assertEquals('!', sequence[3].getValue());

        assertEquals(0, sequence[0].getIndex());
        assertEquals(1, sequence[1].getIndex());
        assertEquals(2, sequence[2].getIndex());
        assertEquals(3, sequence[3].getIndex());

        assertEquals("'a'@0", sequence[0].toString());

    }

    @Test
    void testContext() throws EOFException {

        Token<?>[] content = Sequence.tokenize("ABCDEF");

        Sequence sequence = new Sequence(content);

        assertArrayEquals(content, sequence.getContent());
        assertEquals(7, sequence.getSize());
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
    void testContextMovement() throws EOFException {

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
        Token<?>[] content = Sequence.tokenize("01234");
        Sequence sequence = new Sequence(content);

        sequence.setPointer(3);

        assertArrayEquals(Arrays.copyOfRange(content, 0, 4), sequence.getConsumed());
        assertArrayEquals(Arrays.copyOfRange(content, 3, 6), sequence.getRemainder());

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
        Token<?>[] content = Sequence.tokenize("ABCDEF");

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
    void testTokenEquality() {

        Sequence sequence = Sequence.fromString("AA");

        RootParser<Character, Atom> root = new RootParser<>("", Atom.parser());

        DefinedChar token1 = new DefinedChar(root, sequence.copy(), "A");
        DefinedChar token2 = new DefinedChar(root, sequence.copy(1), "A");

        assertEquals(token1, token2);
    }

    @Test
    void testParent() {

        Factory<DefinedChar> parser = DefinedChar.parser('.');

        RootParser<Character, DefinedChar> root = new RootParser<>("ABC", parser);

        assertDoesNotThrow(root::parse);

        assertEquals(root, root.getToken().getParent());
        assertEquals(root.getToken(), ((INodeToken<?>) root.getToken().getContent()[0]).getParent());
    }

    @Test
    void testRoot() {

        Factory<DefinedChar> parser = DefinedChar.parser('.');

        RootParser<Character, DefinedChar> root = new RootParser<>("ABC", parser);

        assertDoesNotThrow(root::parse);

        assertEquals(root, root.getToken().getParent());
        assertEquals(root, ((INodeToken<?>) root.getToken().getContent()[0]).getRoot());
    }


    @Test
    void testDepth() {

        Factory<DefinedChar> parser = DefinedChar.parser('.');

        RootParser<Character, DefinedChar> root = new RootParser<>("ABC", parser);

        assertEquals(0, root.getContent()[0].getDepth());

        assertDoesNotThrow(root::parse);

        assertEquals(0, root.getDepth());
        assertEquals(1, root.getToken().getDepth());
        assertEquals(2, root.getToken().getContent()[0].getDepth());
    }

    @Test
    void testDefinedChar() {

        Factory<DefinedChar> parser = DefinedChar.parser('.');

        RootParser<Character, DefinedChar> root = new RootParser<>("A", parser);

        assertDoesNotThrow(root::parse);

        assertEquals("A", root.getToken().getWhitelist());
        assertEquals('A', root.getValue());
        assertEquals("A", root.getToken().getRaw());
        assertEquals(0, root.getToken().getIndex());
        assertEquals(1, root.getToken().getSize());
        assertEquals(1, root.getToken().getLength());
        assertEquals(DefinedChar.class, root.getToken().getType());
        assertEquals("(A)@0", root.getToken().toString());

        DefinedChar token = new DefinedChar(new DummyRoot(), Sequence.fromString("A"), "A");
        assertEquals("(?)@0", token.toString());
    }

    @Test
    void testDefinedCharEof() {

        DefinedChar token = new DefinedChar(new DummyRoot(), Sequence.fromString(""), "A");
        assertThrows(EOFParseError.class, token::parse);
    }

    @Test
    void testDefinedCharWhitelist(){

        Factory<DefinedChar> parser = DefinedChar.parser("!:;");

        RootParser<Character, DefinedChar> root = new RootParser<>(":", parser);

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
    void testSpecialChar() {

        RootParser<Character, SpecialChar> root = new RootParser<>("\\n", SpecialChar.parser("tn"));

        assertDoesNotThrow(root::parse);

        assertEquals("tn", root.getToken().getWhitelist());
        assertEquals("\\n", root.getToken().getRaw());
        assertEquals("\\n", root.getToken().getJoin());
        assertArrayEquals(new Character[] {'\\', 'n'}, root.getToken().getValues());
        assertEquals('\n', root.getValue());
        assertEquals("\\n\\@0", root.getToken().toString());

        SpecialChar token = new SpecialChar(new DummyRoot(), Sequence.fromString("\n"), "n");
        assertEquals("(\\?\\)@0", token.toString());
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
    @SuppressWarnings("unchecked")
    void testUnion() {

        Factory<DefinedChar>[] targets = new Factory[]{DefinedChar.parser('.'), DefinedChar.parser('.')};
        Factory<DummyUnion> parser1 = DummyUnion.parser(targets);
        Factory<DefinedChar> parser2 = DefinedChar.parser('.');

        RootParser<Character, DummyUnion> root1 = new RootParser<>("1", parser1);
        RootParser<Character, DefinedChar> root2 = new RootParser<>("1", parser2);

        assertDoesNotThrow(root1::parse);
        assertDoesNotThrow(root2::parse);

        assertEquals('1', root1.getToken().getValue());
        assertEquals(root1.getToken().getToken(), root2.getToken());
        assertEquals(targets, root1.getToken().getTargets());

    }

    @Test
    @SuppressWarnings("unchecked")
    void testUnionError() {

        Factory<DefinedChar>[] targets = new Factory[]{DefinedChar.parser('.'), DefinedChar.parser('.')};
        Factory<DummyUnion> parser = DummyUnion.parser(targets);

        RootParser<Character, DummyUnion> root = new RootParser<>("A", parser, false);

        assertThrows(MultiParseError.class, root::parse);

    }

    @Test
    void testStringLiteral() {

        RootParser<String, StringLiteral> root = new RootParser<>("foo", StringLiteral.parser("foo"));

        assertDoesNotThrow(root::parse);

        assertEquals("foo", root.getToken().getRaw());
        assertEquals("foo", root.getValue());
        assertEquals("foo", root.getToken().getLiteral());
        assertEquals(3, root.getToken().getLength());
        assertEquals(3, root.getToken().getSize());
        assertInstanceOf(Char.class, root.getToken().getTarget());

    }

    @Test
    void testEmptyLiteral() {

        Factory<StringLiteral> parser = StringLiteral.parser("");
        RootParser<String, StringLiteral> root = new RootParser<>("A", parser, false);

        RuntimeException error = assertThrows(RuntimeException.class, root::parse);

        assertEquals(EmptyLiteralException.class, error.getCause().getClass());
    }

    @Test
    void TestStringEnum() {

        Factory<DummyStringEnum> parser = DummyStringEnum.parser("foo", "bar", "zip");

        RootParser<String, DummyStringEnum> root = new RootParser<>("bar", parser);

        assertDoesNotThrow(root::parse);

        assertEquals("bar", root.getValue());
        assertEquals(3, root.getToken().getLength());
        assertEquals(1, root.getToken().getSize());
        assertArrayEquals(new String[]{"foo", "bar", "zip"}, root.getToken().getMembers());

    }

    @Test
    void TestStringEnumError() {

        Factory<DummyStringEnum> parser = DummyStringEnum.parser("foo", "bar", "zip");

        RootParser<String, DummyStringEnum> root = new RootParser<>("zi", parser, false);

        assertThrows(MultiParseError.class, root::parse);

    }

    @Test
    void testStringArray() {

        Factory<StringLiteral> target = StringLiteral.parser("ABC");

        RootParser<String, StringLiteral> testRoot = new RootParser<>("ABC", target);
        assertDoesNotThrow(testRoot::parse);
        StringLiteral testToken = testRoot.getToken();

        Factory<DummyStringArray<StringLiteral>> parser = DummyStringArray.parser(target, 1, 2);

        RootParser<String, DummyStringArray<StringLiteral>> root1 = new RootParser<>("ABC", parser);
        RootParser<String, DummyStringArray<StringLiteral>> root2 = new RootParser<>("ABCABC", parser);
        RootParser<String, DummyStringArray<StringLiteral>> root3 = new RootParser<>("ABCABCABC", parser);
        RootParser<String, DummyStringArray<StringLiteral>> root4 = new RootParser<>("AB", parser);

        assertDoesNotThrow(root1::parse);
        assertDoesNotThrow(root2::parse);
        assertDoesNotThrow(root3::parse);
        assertThrows(UndersizedArrayError.class, root4::parse);

        assertEquals("ABC", root1.getValue());
        assertEquals("ABCABC", root2.getValue());
        assertEquals("ABCABC", root3.getValue());

        assertEquals(1, root1.getToken().getExtentMin());
        assertEquals(2, root1.getToken().getExtentMax());

        assertArrayEquals(new StringLiteral[]{testToken}, root1.getToken().getTokens());
        assertArrayEquals(new StringLiteral[]{testToken, testToken}, root2.getToken().getTokens());
        assertArrayEquals(new StringLiteral[]{testToken, testToken}, root3.getToken().getTokens());

    }

    @Test
    void testIllegalRange() {

        Factory<DummyStringArray<Word>> parser1 = DummyStringArray.parser(Word.parser(), 0, 1);
        Factory<DummyStringArray<Word>> parser2 = DummyStringArray.parser(Word.parser(), 2, 1);

        RootParser<String, DummyStringArray<Word>> root1 = new RootParser<>("A", parser1, false);
        RootParser<String, DummyStringArray<Word>> root2 = new RootParser<>("A", parser2, false);

        RuntimeException error1 = assertThrows(RuntimeException.class, root1::parse);
        RuntimeException error2 = assertThrows(RuntimeException.class, root2::parse);

        assertEquals(IllegalExtentRangeException.class, error1.getCause().getClass());
        assertEquals(IllegalExtentRangeException.class, error2.getCause().getClass());

    }


    @Test
    @SuppressWarnings("unchecked")
    void testStringChain() {

        Factory<StringLiteral> target1 = StringLiteral.parser("ABC");
        RootParser<String, StringLiteral> testRoot1 = new RootParser<>("ABC", target1);
        assertDoesNotThrow(testRoot1::parse);
        StringLiteral testToken1 = testRoot1.getToken();

        Factory<StringLiteral> target2 = StringLiteral.parser("DEF");
        RootParser<String, StringLiteral> testRoot2 = new RootParser<>("DEF", target2);
        assertDoesNotThrow(testRoot2::parse);
        StringLiteral testToken2 = testRoot2.getToken();

        Factory<DummyStringChain<StringLiteral>> parser = DummyStringChain.parser(target1, target2);

        RootParser<String, DummyStringChain<StringLiteral>> root1 = new RootParser<>("ABCDEF", parser);
        RootParser<String, DummyStringChain<StringLiteral>> root2 = new RootParser<>("ABCDEFGHI", parser);
        RootParser<String, DummyStringChain<StringLiteral>> root3 = new RootParser<>("ABC", parser);
        RootParser<String, DummyStringChain<StringLiteral>> root4 = new RootParser<>("ABCDE", parser);

        assertDoesNotThrow(root1::parse);
        assertDoesNotThrow(root2::parse);
        ParserError error1 = assertThrows(ParserError.class, root3::parse);
        ParserError error2 = assertThrows(ParserError.class, root4::parse);

        assertEquals("ABCDEF", root1.getValue());
        assertEquals("ABCDEF", root2.getValue());

        assertArrayEquals(new StringLiteral[]{testToken1, testToken2}, root1.getToken().getTokens());
        assertArrayEquals(new StringLiteral[]{testToken1, testToken2}, root2.getToken().getTokens());
        assertArrayEquals(new Factory[]{target1, target2}, root1.getToken().getTargets());

        assertEquals(EOFParseError.class, error1.getCause().getClass());
        assertEquals(LiteralDoesNotMatch.class, error2.getCause().getClass());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testOptionsChain() {

        Factory<StringLiteral> target1 = StringLiteral.parser("ABC");
        Factory<StringLiteral> target2 = StringLiteral.parser("DEF");

        RootParser<String, StringLiteral> testRoot1 = new RootParser<>("ABC", target1);
        RootParser<String, StringLiteral> testRoot2 = new RootParser<>("DEF", target2);

        assertDoesNotThrow(testRoot1::parse);
        assertDoesNotThrow(testRoot2::parse);

        StringLiteral testToken1 = testRoot1.getToken();
        StringLiteral testToken2 = testRoot2.getToken();

        Factory<DummyOptionsChain<StringLiteral>> parser = DummyOptionsChain.parser(target1, target2);

        RootParser<String, DummyOptionsChain<StringLiteral>> root1 = new RootParser<>("ABCDEF", parser);
        RootParser<String, DummyOptionsChain<StringLiteral>> root2 = new RootParser<>("ABC", parser);
        RootParser<String, DummyOptionsChain<StringLiteral>> root3 = new RootParser<>("DEF", parser);
        RootParser<String, DummyOptionsChain<StringLiteral>> root4 = new RootParser<>("A", parser, false);

        assertDoesNotThrow(root1::parse);
        assertDoesNotThrow(root2::parse);
        assertDoesNotThrow(root3::parse);
        assertThrows(EmptyChainError.class, root4::parse);

        assertArrayEquals(new Factory[]{target1, target2}, root1.getToken().getTargets());

        assertEquals("ABCDEF", root1.getValue());
        assertEquals("ABC", root2.getValue());
        assertEquals("DEF", root3.getValue());

        assertArrayEquals(new StringLiteral[]{testToken1, testToken2}, root1.getToken().getTokens());
        assertArrayEquals(new StringLiteral[]{testToken1}, root2.getToken().getTokens());
        assertArrayEquals(new StringLiteral[]{testToken2}, root3.getToken().getTokens());

        assertArrayEquals(new String[] {"ABC", "DEF"}, root1.getToken().getValues());
        assertArrayEquals(new String[] {"ABC"}, root2.getToken().getValues());
        assertArrayEquals(new String[] {"DEF"}, root3.getToken().getValues());
    }

    @Test
    void testCast() {

        Factory<DummyCast> parser = DummyCast.parser();

        RootParser<Integer, DummyCast> root1 = new RootParser<>("1", parser);

        assertDoesNotThrow(root1::parse);

        assertEquals(1, root1.getValue());

    }

    @Test
    void testTrim() {

        Factory<StringLiteral> target = StringLiteral.parser("ABC");

        RootParser<String, StringLiteral> testRoot = new RootParser<>("ABC", target);
        assertDoesNotThrow(testRoot::parse);
        StringLiteral testToken = testRoot.getToken();

        Factory<Trim<String, StringLiteral>> parser = Trim.parser(target, " ", "\n");

        RootParser<String, Trim<String, StringLiteral>> root1 = new RootParser<>(" ABC", parser);
        RootParser<String, Trim<String, StringLiteral>> root2 = new RootParser<>("ABC\n", parser);
        RootParser<String, Trim<String, StringLiteral>> root3 = new RootParser<>(" ABC\n", parser);
        RootParser<String, Trim<String, StringLiteral>> root4 = new RootParser<>(" ABC", parser);

        assertDoesNotThrow(root1::parse);
        assertDoesNotThrow(root2::parse);
        assertDoesNotThrow(root3::parse);
        assertDoesNotThrow(root4::parse);

        assertEquals(" ", root1.getToken().getLeftBlacklist());
        assertEquals("\n", root1.getToken().getRightBlacklist());
        assertEquals(StringLiteral.class, root1.getToken().getType());

        assertEquals("ABC", root1.getValue());
        assertEquals("ABC", root2.getValue());
        assertEquals("ABC", root3.getValue());
        assertEquals("ABC", root4.getValue());

        assertEquals(target, root1.getToken().getTarget());
        assertEquals(target, root2.getToken().getTarget());
        assertEquals(target, root3.getToken().getTarget());
        assertEquals(target, root4.getToken().getTarget());

        assertEquals(testToken, root1.getToken().getToken());
        assertEquals(testToken, root2.getToken().getToken());
        assertEquals(testToken, root3.getToken().getToken());
        assertEquals(testToken, root4.getToken().getToken());

    }


    @Test
    void testLRTrim() {

        Factory<StringLiteral> target = StringLiteral.parser("ABC");

        Factory<LeftTrim<String, StringLiteral>> parser1 = LeftTrim.parser(target, " ");
        Factory<RightTrim<String, StringLiteral>> parser2 = RightTrim.parser(target, " ");

        RootParser<String, LeftTrim<String, StringLiteral>> root1 = new RootParser<>(" ABC", parser1);
        RootParser<String, RightTrim<String, StringLiteral>> root2 = new RootParser<>("ABC ", parser2);
        RootParser<String, LeftTrim<String, StringLiteral>> root3 = new RootParser<>("ABC", parser1);
        RootParser<String, RightTrim<String, StringLiteral>> root4 = new RootParser<>("ABC", parser2);
        RootParser<String, LeftTrim<String, StringLiteral>> root5 = new RootParser<>(" ABC ", parser1);
        RootParser<String, RightTrim<String, StringLiteral>> root6 = new RootParser<>(" ABC ", parser2, false);

        assertDoesNotThrow(root1::parse);
        assertDoesNotThrow(root2::parse);
        assertDoesNotThrow(root3::parse);
        assertDoesNotThrow(root4::parse);
        assertDoesNotThrow(root5::parse);
        assertThrows(ParserError.class, root6::parse);

    }

    @Test
    void testTrimmed() {

        Factory<StringLiteral> target = StringLiteral.parser("ABC");

        Factory<Trim<String, StringLiteral>> parser = Trim.parser(target, " ", "");

        RootParser<String, Trim<String, StringLiteral>> root1 = new RootParser<>("  ABC", parser);

        assertDoesNotThrow(root1::parse);
        assertEquals(Trimmed.class, root1.getToken().getContent()[0].getClass());

        Trimmed token = (Trimmed) root1.getToken().getContent()[0];
        assertNull(token.getValue());
        assertArrayEquals(new Character[]{' ', ' '}, token.getValues());
        assertEquals("Trimmed@0", token.toString());

    }

    @Test
    void testFactory() {

        assertEquals(DefinedChar.class, DefinedChar.parser('.').getType());

    }

    @Test
    void testChar() {

        RootParser<Character, DefinedChar> inner = new RootParser<>("A", DefinedChar.parser('.'));

        assertDoesNotThrow(inner::parse);

        RootParser<Character, Atom> root1 = new RootParser<>("A", new Char());
        RootParser<Character, Atom> root2 = new RootParser<>(new IToken[]{inner.getToken()}, new Char(), false);

        assertDoesNotThrow(root1::parse);

        assertEquals(Atom.class, root1.getToken().getType());
        assertEquals(new Atom('A', 0), root1.getToken());

        assertThrows(ExpectedGlyphError.class, root2::parse);

    }

    @Test
    void testOptional() {

        Factory<DefinedChar> target = DefinedChar.parser('.');
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
    void testFindFromIndex() {

        Sequence sequence = Sequence.fromString("ABCDEF");

        Factory<StringLiteral> literal1 = StringLiteral.parser("ABC");
        Factory<StringLiteral> literal2 = StringLiteral.parser("DEF");

        Factory<DummyStringChain<StringLiteral>> parser = DummyStringChain.parser(literal1, literal2);

        RootParser<String, DummyStringChain<StringLiteral>> root = new RootParser<>(sequence.copy(), parser);

        assertDoesNotThrow(root::parse);

        assertSame(sequence.getContent()[0], ((ICompositeToken<?>) ((ICompositeToken<?>) root.getContent()[0]).getContent()[0]).getContent()[0]);
        assertSame(sequence.getContent()[0], root.getSequence().getSymbol(0));

        assertEquals("ABC", root.getSequence().getSymbol(0).getParent().getValue());
        assertEquals("DEF", root.getSequence().getSymbol(5).getParent().getValue());



    }

}

