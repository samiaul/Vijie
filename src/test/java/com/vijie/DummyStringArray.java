package com.vijie;

import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.interfaces.IToken;
import com.vijie.core.parsers.Factory;
import com.vijie.core.tokens.StringArray;
import com.vijie.core.tokens.StringEnum;


public class DummyStringArray<T extends IToken<String>> extends StringArray<T> {

    @SuppressWarnings("unchecked")
    public static <T extends IToken<String>> Factory<DummyStringArray<T>> parser(Factory<? extends T> target, Integer extentMin, Integer extentMax) {
        return new Factory<>((Class<DummyStringArray<T>>) (Class<?>) DummyStringArray.class, target, extentMin, extentMax);
    }

    public DummyStringArray(ICompositeToken<?> parent,
                            Sequence sequence,
                            Factory<? extends T> targets,
                            Integer extentMin,
                            Integer extentMax) {
        super(parent, sequence, targets, extentMin, extentMax);
    }

    @Override
    public String getValue() {
        return this.getJoin();
    }
}
