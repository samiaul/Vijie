package com.vijie.core.errors;

import com.vijie.core.Sequence;
import com.vijie.core.interfaces.IFailedToken;


public abstract class GenericFailedTokenError extends BaseParseError {

    protected final IFailedToken failedToken;


    public GenericFailedTokenError(Sequence sequence, IFailedToken failedToken) {
        super(sequence, failedToken.getInterrupter().getMessage(), failedToken.getInterrupter());
        this.failedToken = failedToken;
    }

    public IFailedToken getFailedToken() {
        return failedToken;
    }

    @Override
    public GenericInterrupter getCause() {
        return (GenericInterrupter) super.getCause();
    }

    public abstract int getIndex();
    public abstract int getLength();

}
