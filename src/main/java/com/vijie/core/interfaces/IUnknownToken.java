package com.vijie.core.interfaces;

public interface IUnknownToken<T extends ICompositeToken<?>> extends ICompositeFailedToken {
    IParser<T> getTarget();
}
