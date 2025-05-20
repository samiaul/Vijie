package com.vijie.core.errors;

import com.vijie.core.interfaces.IGenericFailedToken;
import com.vijie.core.interfaces.IToken;

public class FailedTokenInterruption extends RuntimeException {

    private final IGenericFailedToken failedToken;

    public FailedTokenInterruption(IGenericFailedToken failedToken) {
        super("Saved token: " + failedToken);
        this.failedToken = failedToken;
    }

    public GenericFailedTokenError getError() {
        return this.failedToken.getError();
    }

    public IGenericFailedToken getFailedToken() {
        return this.failedToken;
    }

    public <T extends IToken<?>> T getToken() {
        return (T) this.failedToken;
    }

    @Override
    public String toString() {
        return "FailedTokenInterruption(%s, %s)".formatted(this.failedToken, failedToken.getError());
    }
}
