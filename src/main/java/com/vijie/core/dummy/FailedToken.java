package com.vijie.core.dummy;

import com.vijie.core.Sequence;
import com.vijie.core.errors.*;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IFailedToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.symbols.Break;

public class FailedToken<T extends ICompositeToken<?>> extends GenericDummyTargetToken<T> implements IFailedToken<T> {

    protected final Interruption interruption;

    public FailedToken(ICompositeToken<?> parent,
                       Sequence sequence,
                       IParser<T> target,
                       Interruption interruption,
                       IParser<?> syncTarget) {
        super(parent, sequence, target, interruption.getCause(), syncTarget);
        this.interruption = interruption;
    }

    @Override
    public GenericFailedTokenError getError() {
        return new FailedTokenError(this);
    }

    @Override
    public T getToken() {
        return this.sequence.get(0);
    }

    @Override
    public void parse() throws BaseParseError {

        Sequence sequence = this.sequence.copy();

        try {
            sequence.find(this, this.syncTarget);
        } catch (NotFoundError error) {
            throw this.interruption;
        }

        sequence.clearFrom();
        if (sequence.getSize() == 0) throw new EOFInterrupter(sequence);

        this.sequence.clearFrom(sequence.getPointer());
        this.sequence.append(new Break(this, sequence.getEndIndex() - 1));

    }

    @Override
    public String toString() {
        return "Failed(%s)@%d".formatted(this.target, this.getIndex());
    }
}
