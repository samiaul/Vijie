package com.vijie.core.dummy;

import com.vijie.core.NodeToken;
import com.vijie.core.Sequence;
import com.vijie.core.errors.GenericFailedTokenError;
import com.vijie.core.errors.GenericInterrupter;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IDummyToken;
import com.vijie.core.interfaces.IParser;

import javax.lang.model.type.NullType;
import java.util.List;

public abstract class GenericDummyToken extends NodeToken<NullType> implements IDummyToken {

    protected final GenericInterrupter interrupter;
    protected final IParser<?> syncTarget;

    protected GenericDummyToken(ICompositeToken<?> parent,
                                Sequence sequence,
                                GenericInterrupter interrupter,
                                IParser<?> syncTarget) {
        super(parent, sequence);
        this.interrupter = interrupter;
        this.syncTarget = syncTarget;
    }

    @Override
    public NullType getValue() {
        return null;
    }

    @Override
    public GenericInterrupter getInterrupter() {
        return this.interrupter;
    }

    @Override
    public List<GenericFailedTokenError> getErrors() {
        return List.of(this.getError());
    }
}
