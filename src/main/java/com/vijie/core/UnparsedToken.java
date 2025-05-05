package com.vijie.core;

import com.vijie.core.errors.*;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;

import javax.lang.model.type.NullType;
import java.util.List;

public class UnparsedToken<T extends ICompositeToken<?>> extends NodeToken<NullType> {

    protected final T token;
    protected final GenericInterrupter interrupter;
    protected final IParser<?> syncTarget;

    /**
     * Constructs a token with the specified parent node and sequence.
     *
     * @param parent   the parent node
     * @param sequence the sequence associated with this edge
     */
    protected UnparsedToken(ICompositeToken<?> parent,
                            Sequence sequence,
                            T token,
                            GenericInterrupter interrupter,
                            IParser<?> syncTarget) {
        super(parent, sequence);

        this.token = token;
        this.interrupter = interrupter;
        this.syncTarget = syncTarget;
    }

    public T getToken() {
        return token;
    }

    public GenericInterrupter getInterrupter() {
        return interrupter;
    }

    public IParser<?> getSyncTarget() {
        return syncTarget;
    }

    public GenericUnparsedError getError() {
        return new GenericUnparsedError(this.sequence, this.token, this.interrupter);
    }

    @Override
    public NullType getValue() {
        return null;
    }

    @Override
    public void parse() throws BaseParseError {

        while (!sequence.isEof()) {

            try {
                this.sequence.copy().parse(this, this.syncTarget);
            } catch (BaseParseError error) {
                this.sequence.next();
                continue;
            }

            this.sequence.clearFrom();
            return;
        }

        throw this.interrupter;
    }

    @Override
    public String toString() {
        return "Unparsed(%s)@%d".formatted(this.token, this.getIndex());
    }
}
