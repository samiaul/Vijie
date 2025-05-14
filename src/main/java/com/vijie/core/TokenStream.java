package com.vijie.core;

import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IGenericFailedToken;
import com.vijie.core.interfaces.IToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Stream;

public class TokenStream<V, T extends IToken<V>> {

    private final List<T> content;

    public TokenStream(List<T> content) {
        this.content = content;
    }

    public TokenStream(T[] content) {
        this(Arrays.stream(content).toList());
    }

    public static <W, T extends IToken<W>> TokenStream<W, T> of(T[] content) {
        return new TokenStream(content);
    }

    public static <T extends IToken<?>> TokenStream<?, T> erasedOf(T[] content) {
        return new TokenStream(content);
    }

    public static TokenStream<?, IToken<?>> of(ICompositeToken<?> source) {
        return source.getSequence().tokenStream();
    }

    public TokenStream<V, T> curate() {
        List<T> filtered = new ArrayList<>();
        for (T item : content) {
            if (item instanceof IGenericFailedToken) continue;
            filtered.add(item);
        }

        return new TokenStream(filtered);
    }

    @SuppressWarnings("unchecked")
    public <W, K extends IToken<W>> TokenStream<W, K> filter(Class<K> tokenType) {
        List<K> filtered = new ArrayList<>();
        for (T item : content) {
            if (item.matches(tokenType)) {
                filtered.add((K) item);
            }
        }
        return new TokenStream(filtered);
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public final <K extends IToken<?>> TokenStream<?, K> filter(Class<K> type, Class<? extends K>... tokenTypes) {
        List<K> filtered = new ArrayList<>();
        for (T item : content) {
            if (item.matches(tokenTypes)) filtered.add((K) item);
        }
        return new TokenStream(filtered);
    }

    public <O> Stream<O> flatMap(Function<T, List<O>> mapper) {
        List<O> flatMapped = new ArrayList<>();
        for (T item : content) {
            flatMapped.addAll(mapper.apply(item));
        }
        return flatMapped.stream();
    }

    public final List<V> getValues() {
        List<V> values = new ArrayList<>();
        for (T item : content) {
            if (item.getValue() != null) values.add(item.getValue());
        }
        return values;
    }

    public final V[] getValues(IntFunction<V[]> generator) {
        return this.getValues().toArray(generator);
    }

    public List<T> toList() {
        return new ArrayList<>(content);
    }

    public T[] toArray(IntFunction<T[]> generator) {
        return content.toArray(generator);
    }
}
