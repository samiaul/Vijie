package com.vijie.core.symbols;

import com.vijie.core.Token;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.INodeToken;
import com.vijie.core.interfaces.IToken;

import javax.lang.model.type.NullType;

public class EOF extends Symbol<NullType> {

    public EOF(int index) {
        super(null, index, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRaw() {
        return "";
    }

    @Override
    public String toString() {
        return "EOF@%d".formatted(index);
    }

}

