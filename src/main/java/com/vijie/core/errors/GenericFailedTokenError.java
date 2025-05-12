package com.vijie.core.errors;

import com.vijie.core.Sequence;
import com.vijie.core.interfaces.IGenericFailedToken;


public abstract class GenericFailedTokenError extends BaseParseError {

    protected final IGenericFailedToken failedToken;


    public GenericFailedTokenError(Sequence sequence, IGenericFailedToken failedToken) {
        super(sequence, failedToken.getInterrupter().getMessage(), failedToken.getInterrupter());
        this.failedToken = failedToken;
    }

    public IGenericFailedToken getFailedToken() {
        return failedToken;
    }

    @Override
    public GenericInterrupter getCause() {
        return (GenericInterrupter) super.getCause();
    }

    public abstract int getIndex();
    public abstract int getLength();

}
