package com.vijie;

import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.parsers.Factory;
import com.vijie.core.tokens.DefinedChar;
import com.vijie.core.tokens.Union;

public class DummyUnion extends Union<Character, DefinedChar> {

    public static Factory<DummyUnion> parser(Factory<DefinedChar>[] targets) {
        return Factory.of(DummyUnion.class, (Object) targets);
    }

    public DummyUnion(ICompositeToken<?> parent, Sequence sequence, Factory<DefinedChar>[] targets) {
        super(parent, sequence, targets);
    }
}
