package com.vijie.core;

import com.vijie.core.errors.*;
import com.vijie.core.interfaces.ICompositeFailedToken;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IFailedToken;
import com.vijie.core.interfaces.IParser;

import javax.lang.model.type.NullType;
import java.util.List;

public class UnknownToken<T extends ICompositeToken<?>> extends NodeToken<NullType> implements ICompositeFailedToken {

    protected final IParser<T> target;
    protected final GenericInterrupter interrupter;
    protected final IParser<?> syncTarget;

    /**
     * Constructs a token with the specified parent node and sequence.
     *
     * @param parent   the parent node
     * @param sequence the sequence associated with this edge
     */
    public UnknownToken(ICompositeToken<?> parent,
                        Sequence sequence,
                        IParser<T> target,
                        GenericInterrupter interrupter,
                        IParser<?> syncTarget) {
        super(parent, sequence);

        this.target = target;
        this.interrupter = interrupter;
        this.syncTarget = syncTarget;
    }

    public IParser<T> getTarget() {
        return target;
    }

    public GenericInterrupter getInterrupter() {
        return interrupter;
    }

    public GenericFailedTokenError getError() {
        return new FailedTokenError(this);
    }

    @Override
    public NullType getValue() {
        return null;
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
