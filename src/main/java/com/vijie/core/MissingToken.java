package com.vijie.core;

import com.vijie.core.errors.*;
import com.vijie.core.interfaces.*;

import javax.lang.model.type.NullType;

public class MissingToken<T extends IToken<?>> extends Token<NullType> implements INodeToken<NullType>, IGenericFailedToken {

    protected final ICompositeToken<?> parent;
    protected final int index;
    protected final IParser<T> target;
    protected final MissingTokenInterrupter interrupter;

    public MissingToken(ICompositeToken<?> parent,
                        int index,
                        IParser<T> target,
                        MissingTokenInterrupter interrupter) {

        this.parent = parent;
        this.index = index;
        this.target = target;
        this.interrupter = interrupter;
    }

    public MissingTokenInterrupter getInterrupter() {
        return interrupter;
    }

    public GenericFailedTokenError getError() {
        return new MissingTokenError(this);
    }

    @Override
    public String getRaw() {
        return "";
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public ICompositeToken<?> getParent() {
        return this.parent;
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public int getDepth() {
        return this.parent.getDepth() + 1;
    }

    @Override
    public NullType getValue() {
        return null;
    }

    @Override
    public String getSequenceRepr() {
        return "";
    }

    @Override
    public String toString() {
        return "Missing(%s)@%d".formatted(this.target, this.getIndex());
    }
}
