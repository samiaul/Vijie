package com.vijie;

import com.vijie.core.Root;
import com.vijie.core.errors.BaseParseError;
import com.vijie.core.errors.GenericParseError;
import com.vijie.core.errors.OptionalNotFound;
import com.vijie.core.errors.ParserError;
import com.vijie.core.interfaces.IParser;
import com.vijie.core.interfaces.IToken;

public class DummyRoot extends Root<Object> {

    protected DummyRoot() {
        super("");
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void parse() throws BaseParseError {}
}
