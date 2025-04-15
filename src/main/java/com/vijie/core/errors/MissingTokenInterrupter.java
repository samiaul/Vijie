package com.vijie.core.errors;

import com.vijie.core.Sequence;

public class MissingTokenInterrupter extends PendingInterrupter {
    public MissingTokenInterrupter(Sequence sequence, String message) {
        super(sequence, message);
    }
}
