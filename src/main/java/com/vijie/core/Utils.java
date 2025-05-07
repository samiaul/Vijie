package com.vijie.core;

import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.interfaces.IToken;
import com.vijie.core.parsers.Factory;
import com.vijie.core.tokens.DefinedChar;
import com.vijie.core.tokens.StringLiteral;
import com.vijie.core.tokens.Trimmed;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Utils {

    /**
     * Prepends an object to the beginning of an array.
     *
     * @param parent the node parent
     * @param sequence the token sequence
     * @param rest the array of custom params
     * @return a new array with the params prepended
     */
    public static Object[] prependParam(ICompositeToken<?> parent, Sequence sequence, Object[] rest) {
        Object[] result = new Object[rest.length + 2];
        result[0] = parent;
        result[1] = sequence;
        System.arraycopy(rest, 0, result, 2, rest.length);
        return result;
    }

    /**
     * Prepends a class type to the beginning of an array of class types.
     *
     * @param rest the array of class types to which the class type will be prepended
     * @return a new array with the class type prepended
     */
    public static Class<?>[] prependParamType(Object[] rest) {
        Class<?>[] types = inferParamsTypes(rest);
        Class<?>[] result = new Class<?>[types.length + 2];
        result[0] = ICompositeToken.class;
        result[1] = Sequence.class;
        System.arraycopy(types, 0, result, 2, types.length);
        return result;
    }

    /**
     * Infers the class types of the given parameters.
     *
     * @param params the parameters whose class types are to be inferred
     * @return an array of class types of the given parameters
     */
    public static Class<?>[] inferParamsTypes(Object... params) {
        return Arrays.stream(params)
                .map(Object::getClass)
                .toArray(Class<?>[]::new);
    }

    /**
     * Merges multiple parsers into a single array.
     *
     * @param first the first parser
     * @param second the second parser
     * @param others additional parsers
     * @return an array containing all the given parsers
     */
    public static IParser<?>[] mergeParsers(IParser<?> first, IParser<?> second, IParser<?>... others) {
        return Stream.concat(
                Stream.of(first, second),
                Arrays.stream(others)
        ).toArray(IParser<?>[]::new);
    }

    /**
     * Merges an array of string literals into an array of parsers.
     *
     * @param literals the string literals to be merged
     * @return an array of parsers for the given string literals
     */
    @SuppressWarnings("unchecked")
    public static Factory<StringLiteral>[] mergeLiterals(String[] literals) {
        return Arrays
                .stream(literals)
                .map(StringLiteral::parser)
                .toArray(Factory[]::new);
    }

    /**
     * Merges multiple strings into a single array.
     *
     * @param first the first string
     * @param second the second string
     * @param others additional strings
     * @return an array containing all the given strings
     */
    public static String[] mergeStrings(String first, String second, String... others) {
        return Stream.concat(
                Stream.of(first, second),
                Arrays.stream(others)
        ).toArray(String[]::new);
    }

    /**
     * A predicate implementation that filters out tokens of type `Trimmed`.
     */
    public static class filterTrims implements Predicate<IToken<?>> {

        /**
         * Tests whether the given token is not of type `Trimmed`.
         *
         * @param token the token to test
         * @return true if the token is not of type `Trimmed`, false otherwise
         */
        @Override
        public boolean test(IToken<?> token) {
            return !(token.matches(Trimmed.class));
        }
    }

    /**
     * A predicate implementation that filters tokens based on a blacklist of characters.
     */
    public static class filterChars implements Predicate<IToken<?>> {

        /**
         * The blacklist of characters to filter.
         */
        private final String blacklist;

        /**
         * Constructs a new filterChars predicate.
         *
         * @param blacklist the string containing characters to be blacklisted
         */
        public filterChars(String blacklist) {
            this.blacklist = blacklist;
        }

        /**
         * Tests whether the given token is not a blacklisted character.
         *
         * @param token the token to test
         * @return true if the token is not a blacklisted character, false otherwise
         */
        @Override
        public boolean test(IToken<?> token) {
            return !(token.matches(DefinedChar.class) && this.blacklist.contains(token.getValue().toString()));
        }
    }

    /**
     * A predicate implementation that filters tokens based on their type.
     *
     * @param <T> the type of token to filter
     */
    public static class filterTokens<T extends IToken<?>> implements Predicate<IToken<?>> {

        /**
         * The target class type of the token to filter.
         */
        private final Class<? extends T>[] targets;

        /**
         * Constructs a new filterTokens predicate.
         *
         * @param targets the class types of the token to filter
         */
        @SafeVarargs
        public filterTokens(Class<? extends T>... targets) {
            this.targets = targets;
        }

        /**
         * Tests whether the given token matches the target class type.
         *
         * @param token the token to test
         * @return true if the token matches the target class type, false otherwise
         */
        @Override
        public boolean test(IToken<?> token) {
            return token.matches(this.targets);
        }
    }

}
