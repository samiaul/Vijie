package com.vijie.core.interfaces;

import com.vijie.core.errors.GenericInterrupter;

import javax.lang.model.type.NullType;

public interface ICompositeFailedToken extends IFailedToken, ICompositeToken<NullType> {}
