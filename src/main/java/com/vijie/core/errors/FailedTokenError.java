package com.vijie.core.errors;

import com.vijie.core.interfaces.IDummyToken;

public class FailedTokenError extends GenericFailedTokenError {

    public FailedTokenError(IDummyToken failedToken) {
        super(failedToken.getSequence(), failedToken);
    }

    @Override
    public int getIndex() {
        return this.failedToken.getIndex();
    }

    public int getLength() {
        return this.failedToken.getLength();
    }

}
