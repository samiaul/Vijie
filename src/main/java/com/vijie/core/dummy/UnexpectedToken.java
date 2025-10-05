package com.vijie.core.dummy;

import com.vijie.core.Sequence;
import com.vijie.core.errors.*;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;

public class UnexpectedToken extends GenericDummyToken {

    public UnexpectedToken(ICompositeToken<?> parent,
                           Sequence sequence,
                           GenericInterrupter interrupter,
                           IParser<?> syncTarget) {
        super(parent, sequence, interrupter, syncTarget);
    }

    @Override
    public UnexpectedTokenError getError() {
        return new UnexpectedTokenError(this);
    }

    @Override
    public void parse() throws BaseParseError {

        this.sequence.find(this, this.syncTarget);

        this.sequence.clearFrom();
        if (this.sequence.getSize() == 0) throw new EOFInterrupter(sequence);

    }

    @Override
    public String toString() {
        return "Unexpected(\"%s\")@%d".formatted(this.getRaw(), this.getIndex());
    }
}
