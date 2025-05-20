package com.vijie.core.symbols;

import com.vijie.core.interfaces.ICompositeToken;

public final class Break extends EOF {

    public Break(ICompositeToken<?> parent, int index) {
        super(index);
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "Break@%d".formatted(index);
    }

}

