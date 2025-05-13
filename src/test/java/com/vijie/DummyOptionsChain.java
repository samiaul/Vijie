package com.vijie;

import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.interfaces.IToken;
import com.vijie.core.parsers.Factory;
import com.vijie.core.tokens.OptionsChain;
import com.vijie.core.tokens.StringChain;

import java.util.Arrays;
import java.util.stream.Collectors;

public class DummyOptionsChain<T extends IToken<String>> extends OptionsChain<String, T> {

    @SuppressWarnings("unchecked")
    public static <T extends IToken<String>> Factory<DummyOptionsChain<T>> parser(IParser<? extends T>... targets) {
        return Factory.of(DummyOptionsChain.class, (Object) targets);
    }

    public DummyOptionsChain(ICompositeToken<?> parent, Sequence sequence, IParser<? extends T>[] targets) {
        super(parent, sequence, targets);
    }

    public String getJoin() {
        return Arrays
                .stream(this.getValues())
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    @Override
    public String getValue() {
        return this.getJoin();
    }
}
