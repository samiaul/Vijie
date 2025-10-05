package com.vijie.core.dummy;

import com.vijie.core.Sequence;
import com.vijie.core.errors.*;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.interfaces.ITargetDummyToken;

public abstract class GenericDummyTargetToken<T extends ICompositeToken<?>> extends GenericDummyToken implements ITargetDummyToken<T> {

    protected final IParser<T> target;

    protected GenericDummyTargetToken(ICompositeToken<?> parent,
                                      Sequence sequence,
                                      IParser<T> target,
                                      GenericInterrupter interrupter,
                                      IParser<?> syncTarget) {
        super(parent, sequence, interrupter, syncTarget);

        this.target = target;
    }

    @Override
    public IParser<T> getTarget() {
        return target;
    }

}
