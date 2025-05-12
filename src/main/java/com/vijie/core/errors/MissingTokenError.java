package com.vijie.core.errors;

import com.vijie.core.MissingToken;
import com.vijie.core.Sequence;
import com.vijie.core.interfaces.IToken;

import java.util.List;


public class MissingTokenError extends GenericFailedTokenError {

    public MissingTokenError(MissingToken<?> missingToken) {
        super(Sequence.fromString(""), missingToken);
    }

    @Override
    public int getIndex() {
        return this.failedToken.getIndex();
    }

    public int getLength() {
        return 1;
    }

}
