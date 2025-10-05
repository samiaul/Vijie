package com.vijie.core.dummy;

import com.vijie.core.Sequence;
import com.vijie.core.errors.*;
import com.vijie.core.interfaces.*;
import com.vijie.core.symbols.Epsilon;

import javax.lang.model.type.NullType;
import java.util.ArrayList;
import java.util.List;

public class MissingToken<T extends IToken<?>> extends GenericDummyToken {

    public MissingToken(ICompositeToken<?> parent,
                        Sequence sequence,
                        IParser<T> target,
                        MissingTokenInterrupter interrupter) {
        super(parent, sequence, interrupter, target);

    }

    public GenericFailedTokenError getError() {
        return new MissingTokenError(this);
    }

    @Override
    public List<GenericFailedTokenError> getErrors() {
        return new ArrayList<>();
    }

    @Override
    public void parse() {
        this.sequence.fusion(new Epsilon(this, this.sequence.getCurrentIndex()));
        this.sequence.clearRemainder();
    }

    @Override
    public String toString() {
        return "Missing(%s)@%d".formatted(this.syncTarget, this.getIndex());
    }
}
