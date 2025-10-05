package com.vijie.core.interfaces;

import com.vijie.core.errors.GenericFailedTokenError;
import com.vijie.core.errors.GenericInterrupter;

import javax.lang.model.type.NullType;

public interface IDummyToken extends ICompositeToken<NullType> {
    GenericInterrupter getInterrupter();
    GenericFailedTokenError getError();
}
