package com.vijie.core.dummy;

import com.vijie.core.Sequence;
import com.vijie.core.errors.*;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.interfaces.ITargetDummyToken;

public class UnknownToken<T extends ICompositeToken<?>> extends GenericDummyTargetToken<T> implements ITargetDummyToken<T> {

    public UnknownToken(ICompositeToken<?> parent,
                        Sequence sequence,
                        IParser<T> target,
                        GenericInterrupter interrupter,
                        IParser<?> syncTarget) {
        super(parent, sequence, target, interrupter, syncTarget);

    }

    @Override
    public FailedTokenError getError() {
        return new FailedTokenError(this);
    }
    @Override
    public void parse() throws GenericInterrupter {

        try {
            this.sequence.find(this, this.syncTarget);
        } catch (NotFoundError error) {
            throw this.interrupter;
        }

        this.sequence.clearFrom();
        if (this.sequence.getSize() == 0) throw new EOFInterrupter(sequence);

    }

    @Override
    public String toString() {
        return "Unknown(%s)@%d".formatted(this.target, this.getIndex());
    }
}
