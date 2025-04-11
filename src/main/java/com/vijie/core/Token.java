package com.vijie.core;

import com.vijie.core.interfaces.IToken;

import java.util.Objects;
import java.util.stream.StreamSupport;

/**
 * Abstract class representing a token with a value of type V.
 *
 * @param <V> the type of the value returned by this token
 */
public abstract class Token<V> implements IToken<V> {

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract int getIndex();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract int getLength();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract int getDepth();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract V getValue();

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends IToken<V>> getType() {
        return (Class<? extends IToken<V>>) this.getClass();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(IToken<?> other) {
        if (this.getClass() == other.getClass()) return this.getValue() == other.getValue();
        return Objects.equals(this.getRaw(), other.getRaw());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof IToken) return equals((IToken<?>) other);
        return this.getValue() == other;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(String value) {
        return this.getRaw().equals(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(Class<? extends IToken<?>> other) {
        return other.isAssignableFrom(this.getType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(Iterable<Class<? extends IToken<?>>> other) {
        return StreamSupport
                .stream(other.spliterator(), false)
                .anyMatch(this::matches);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTreeRepr() {
        String tab = "\t".repeat(this.getDepth());
        return "%s%s".formatted(tab, this.toString());
    }

}
