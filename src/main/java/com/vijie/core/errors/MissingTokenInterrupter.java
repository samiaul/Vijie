package com.vijie.core.errors;

import com.vijie.core.Sequence;
import com.vijie.core.interfaces.IToken;

public class MissingTokenInterrupter extends FatalInterrupter {

    public MissingTokenInterrupter(Sequence sequence, String message) {
        super(sequence, message);
    }
}
