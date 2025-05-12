package com.vijie.core.parsers;

import com.vijie.core.CompositeToken;
import com.vijie.core.Sequence;
import com.vijie.core.UnparsedToken_;
import com.vijie.core.errors.BaseParseError;
import com.vijie.core.errors.EOFInterrupter;
import com.vijie.core.errors.Interruption;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;

public class Unparser_<T extends ICompositeToken<?>, U extends UnparsedToken_<T>> extends Factory<T> {

    protected final Class<U> unparsedType;
    protected final IParser<?> syncTarget;

    public Unparser_(Class<T> tokenType,
                     Class<U> unparsedType,
                     IParser<?> syncTarget,
                     Object... params) {

        super(tokenType, params);

        this.unparsedType = unparsedType;
        this.syncTarget = syncTarget;
    }

    @Override
    public T parse(ICompositeToken<?> parent, Sequence sequence) throws BaseParseError {

        try {
            return super.parse(parent, sequence.copy());
        } catch (Interruption interruption) {
            throw this.unparse(parent, sequence.copy(), interruption);
        }

    }

    protected Interruption unparse(ICompositeToken<?> parent, Sequence sequence, Interruption interruption) {

        boolean flag = this.findSync(parent, sequence);

        if (!flag) return interruption;

        U unparsed = CompositeToken.instantiate(this.unparsedType, parent, sequence, interruption);

        return new Interruption(new EOFInterrupter(sequence) /*unparsed.getError()*/, unparsed);

    }

    protected boolean findSync(ICompositeToken<?> parent, Sequence sequence) {

        while (!sequence.isEof()) {

            try {
                sequence.copy().parse(parent, this.syncTarget);
            } catch (BaseParseError error) {
                sequence.next();
                continue;
            }

            sequence.clearFrom();
            return true;
        }

        return false;

    }
}
