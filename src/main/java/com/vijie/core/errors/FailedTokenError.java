package com.vijie.core.errors;

import com.vijie.core.interfaces.ICompositeFailedToken;

public class FailedTokenError extends GenericFailedTokenError {

    public FailedTokenError(ICompositeFailedToken failedToken) {
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
