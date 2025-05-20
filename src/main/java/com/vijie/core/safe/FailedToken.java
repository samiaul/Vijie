package com.vijie.core.safe;

import com.vijie.core.NodeToken;
import com.vijie.core.Sequence;
import com.vijie.core.errors.*;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IFailedToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.symbols.Break;

import javax.lang.model.type.NullType;

public class FailedToken<T extends ICompositeToken<?>> extends NodeToken<NullType> implements IFailedToken<T> {

    protected final IParser<T> target;
    protected final GenericInterrupter interrupter;
    protected final IParser<?> syncTarget;

    /**
     * Constructs a token with the specified parent node and sequence.
     *
     * @param parent   the parent node
     * @param sequence the sequence associated with this edge
     */
    public FailedToken(ICompositeToken<?> parent,
                       Sequence sequence,
                       IParser<T> target,
                       GenericInterrupter interrupter,
                       IParser<?> syncTarget) {
        super(parent, sequence);

        this.target = target;
        this.interrupter = interrupter;
        this.syncTarget = syncTarget;
    }

    @Override
    public IParser<T> getTarget() {
        return target;
    }

    @Override
    public T getToken() {
        return this.sequence.get(0);
    }

    @Override
    public GenericInterrupter getInterrupter() {
        return interrupter;
    }

    @Override
    public GenericFailedTokenError getError() {
        return new FailedTokenError(this);
    }

    @Override
    public NullType getValue() {
        return null;
    }

    @Override
    public void parse() throws GenericInterrupter {

        boolean toThrow = false;

        Sequence sequence = this.sequence.copy();

        try {
            sequence.find(this, this.syncTarget);
        } catch (NotFoundError error) {
            toThrow = true;
        }

        sequence.clearFrom();
        if (sequence.getSize() == 0) throw new EOFInterrupter(sequence);

        if (!toThrow) {
            this.sequence.clearFrom(sequence.getPointer());
            this.sequence.append(new Break(this, sequence.getEndIndex() - 1));
        }

        try {
            this.sequence.parseAndStep(this, this.target);
        } catch (GenericInterrupter interrupter) {
            if (toThrow) throw new Interruption(interrupter, this.getToken());
        } catch (BaseParseError error) {
            throw new RuntimeException(error);
        }

    }

    @Override
    public String toString() {
        return "Failed(%s)@%d".formatted(this.target, this.getIndex());
    }
}
