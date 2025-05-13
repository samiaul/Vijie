package com.vijie;

import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.parsers.Factory;
import com.vijie.core.tokens.Cast;
import com.vijie.core.tokens.DefinedChar;
import com.vijie.core.tokens.Digit;
import com.vijie.core.tokens.Union;

public class DummyCast extends Cast<Integer, Character, Digit> {

    public static Factory<DummyCast> parser() {
        return Factory.of(DummyCast.class);
    }

    public DummyCast(ICompositeToken<?> parent, Sequence sequence) {
        super(parent, sequence, Digit.parser());
    }

    @Override
    public Integer getValue() {
        return Integer.parseInt(this.getToken().getValue().toString());
    }
}
