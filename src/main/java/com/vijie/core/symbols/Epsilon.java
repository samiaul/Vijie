package com.vijie.core.symbols;

import com.vijie.core.Sequence;
import com.vijie.core.errors.BaseParseError;
import com.vijie.core.interfaces.ICompositeToken;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.interfaces.IToken;

import javax.lang.model.type.NullType;

/**
 * Represents an empty character.
 */
public final class Epsilon extends Symbol<NullType> {

    public Epsilon(ICompositeToken<?> parent, int index) {
        super(null, index, 0);
        this.parent = parent;
    }

    public Epsilon(int index) {
        super(null, index, 0);
    }

    public static IParser<Epsilon> parser() {
        return new Parser();
    }

    @Override
    public String getRaw() {
        return "";
    }

    @Override
    public String toString() {
        return "Epsilon@%d".formatted(index);
    }

    private static class Parser implements IParser<Epsilon> {

        @Override
        public Class<? extends Epsilon> getType() {
            return Epsilon.class;
        }

        @Override
        public Epsilon parse(ICompositeToken<?> parent, Sequence sequence) {
            return new Epsilon(sequence.getCurrentIndex());
        }

        @Override
        public String toString() {
            return "{Epsilon}";
        }
    }
}

