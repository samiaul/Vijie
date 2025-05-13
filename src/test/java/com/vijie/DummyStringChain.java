package com.vijie;

import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.interfaces.IToken;
import com.vijie.core.parsers.Factory;
import com.vijie.core.tokens.StringChain;
import com.vijie.core.tokens.StringEnum;

public class DummyStringChain<T extends IToken<String>> extends StringChain<T> {

    @SuppressWarnings("unchecked")
    public static <T extends IToken<String>> Factory<DummyStringChain<T>> parser(IParser<? extends T>... targets) {
        return Factory.of(DummyStringChain.class, (Object) targets);
    }

    public DummyStringChain(ICompositeToken<?> parent, Sequence sequence, IParser<? extends T>[] targets) {
        super(parent, sequence, targets);
    }

    @Override
    public String getValue() {
        return this.getJoin();
    }
}
