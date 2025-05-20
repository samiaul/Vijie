package com.vijie.core.symbols;

import com.vijie.core.Token;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.INodeToken;
import com.vijie.core.interfaces.ISymbol;
import com.vijie.core.interfaces.IToken;

public abstract class Symbol<V> extends Token<V> implements INodeToken<V>, ISymbol<V> {

    protected ICompositeToken<?> parent;
    protected final V value;
    protected final int index;
    protected final int length;

    protected Symbol(V value, int index, int length) {
        this.value = value;
        this.index = index;
        this.length = length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract String getRaw();

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIndex() {
        return this.index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLength() {
        return this.length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ICompositeToken<?> getParent() {
        return this.parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setParent(ICompositeToken<?> parent) {
        this.parent = parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDepth() {
        if (this.parent == null) return 0;
        return this.parent.getDepth() + 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V getValue() {
        return this.value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSequenceRepr() {
        return this.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends IToken<?>> T findParent(Class<T> clazz) {
        if (clazz.isInstance(this.parent)) return (T) this.parent;
        if (this.parent instanceof INodeToken<?> node) return node.findParent(clazz);
        return null;
    }
}
