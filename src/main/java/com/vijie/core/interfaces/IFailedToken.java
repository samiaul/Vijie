package com.vijie.core.interfaces;

public interface IFailedToken<T extends ICompositeToken<?>> extends ITargetDummyToken<T> {

    T getToken();
}
