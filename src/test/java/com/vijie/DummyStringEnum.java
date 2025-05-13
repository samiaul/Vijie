package com.vijie;

import com.vijie.core.Sequence;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.parsers.Factory;
import com.vijie.core.tokens.StringEnum;

public class DummyStringEnum extends StringEnum {

    public static Factory<DummyStringEnum> parser(String... members) {
        return Factory.of(DummyStringEnum.class, (Object) members);
    }

    public DummyStringEnum(ICompositeToken<?> parent, Sequence sequence, String[] members) {
        super(parent, sequence, members);
    }
}
