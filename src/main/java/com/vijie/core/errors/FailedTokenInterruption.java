package com.vijie.core.errors;

import com.vijie.core.interfaces.IFailedToken;
import com.vijie.core.interfaces.IToken;

public class FailedTokenInterruption extends RuntimeException {

    private final IFailedToken failedToken;

    public FailedTokenInterruption(IFailedToken failedToken) {
        super("Saved token: " + failedToken);
        this.failedToken = failedToken;
    }

    public GenericFailedTokenError getError() {
        return this.failedToken.getError();
    }

    public IFailedToken getFailedToken() {
        return this.failedToken;
    }

    public <T extends IToken<?>> T getToken() {
        return (T) this.failedToken;
    }
}
