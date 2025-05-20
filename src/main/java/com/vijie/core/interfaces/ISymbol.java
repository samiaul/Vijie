package com.vijie.core.interfaces;

public interface ISymbol<V> extends INodeToken<V> {

    void setParent(ICompositeToken<?> parent);

}
