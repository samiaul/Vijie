package com.vijie.core.safe;

import com.vijie.core.NodeToken;
import com.vijie.core.Sequence;
import com.vijie.core.errors.*;
import com.vijie.core.interfaces.*;
import com.vijie.core.symbols.Epsilon;

import javax.lang.model.type.NullType;

public class MissingToken<T extends IToken<?>> extends NodeToken<NullType> implements ICompositeFailedToken {

    protected final IParser<T> target;
    protected final MissingTokenInterrupter interrupter;

    public MissingToken(ICompositeToken<?> parent,
                        Sequence sequence,
                        IParser<T> target,
                        MissingTokenInterrupter interrupter) {

        super(parent, sequence);

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
    public NullType getValue() {
        return null;
    }

    @Override
    public void parse() {
        this.sequence.fusion(new Epsilon(this, this.sequence.getCurrentIndex()));
        this.sequence.clearRemainder();
    }

    @Override
    public String toString() {
        return "Missing(%s)@%d".formatted(this.target, this.getIndex());
    }
}
