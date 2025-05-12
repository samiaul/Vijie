package com.vijie.core.errors;

import com.vijie.core.UnexpectedToken;
import com.vijie.core.interfaces.ICompositeFailedToken;

public class UnexpectedTokenError extends GenericFailedTokenError {

    public UnexpectedTokenError(UnexpectedToken failedToken) {
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
