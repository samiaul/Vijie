package com.vijie.core;

import com.vijie.core.errors.*;
import com.vijie.core.interfaces.ICompositeToken;

import javax.lang.model.type.NullType;

public class UnparsedToken_<T extends ICompositeToken<?>> extends NodeToken<NullType> {

    protected final T token;
    protected final GenericInterrupter interrupter;

    /**
     * Constructs a token with the specified parent node and sequence.
     *
     * @param parent   the parent node
     * @param sequence the sequence associated with this edge
     */
    public UnparsedToken_(ICompositeToken<?> parent,
                          Sequence sequence,
                          Interruption interruption) {
        super(parent, sequence);

        this.token = interruption.getToken();
        this.interrupter = (GenericInterrupter) interruption.getCause();
    }

    public T getToken() {
        return token;
    }

    public GenericInterrupter getInterrupter() {
        return interrupter;
    }

    public GenericUnparsedError_ getError() {
        return new GenericUnparsedError_(this.sequence, this.token, this.interrupter);
    }

    @Override
    public NullType getValue() {
        return null;
    }

    @Override
    public void parse() throws BaseParseError {
    }

    @Override
    public String toString() {
        return "Unparsed(%s)@%d".formatted(this.token, this.getIndex());
    }
}
