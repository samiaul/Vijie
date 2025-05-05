package com.vijie.core.errors;

import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;


public class GenericUnparsedError extends BaseParseError {

    protected final ICompositeToken<?> token;

    public GenericUnparsedError(Sequence sequence, ICompositeToken<?> token, GenericInterrupter interrupter) {
        super(sequence, "Could not parse token \"%s\": %s".formatted(token, interrupter.getMessage()), interrupter);
        this.token = token;
    }

    public ICompositeToken<?> getToken() {
        return this.token;
    }

    @Override
    public GenericInterrupter getCause() {
        return (GenericInterrupter) super.getCause();
    }

    @Override
    public int getIndex() {
        return this.getSequence().getStartIndex();
    }

    public int getLength() {
        return this.getSequence().getLength();
    }

}
