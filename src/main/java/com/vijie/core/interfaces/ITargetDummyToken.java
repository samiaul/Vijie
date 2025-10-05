package com.vijie.core.interfaces;

public interface ITargetDummyToken<T extends ICompositeToken<?>> extends IDummyToken {

    IParser<T> getTarget();

}
