package com.vijie.core.errors;

import com.vijie.core.Sequence;
import com.vijie.core.interfaces.IDummyToken;


public abstract class GenericFailedTokenError extends BaseParseError {

    protected final IDummyToken failedToken;


    public GenericFailedTokenError(Sequence sequence, IDummyToken failedToken) {
        super(sequence, failedToken.getInterrupter().getMessage(), failedToken.getInterrupter());
        this.failedToken = failedToken;
    }

    public IDummyToken getFailedToken() {
        return failedToken;
    }

    @Override
    public GenericInterrupter getCause() {
        return (GenericInterrupter) super.getCause();
    }

    public abstract int getIndex();
    public abstract int getLength();

}
