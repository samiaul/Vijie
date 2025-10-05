package com.vijie.core.errors;

import com.vijie.core.Sequence;
import com.vijie.core.interfaces.IParser;

public class NotFoundError extends GenericParseError {

    private final IParser<?> target;

    public NotFoundError(Sequence sequence, IParser<?> target) {
        super(sequence, "Target not found: " + target.toString());
        this.target = target;
    }

    public IParser<?> getTarget() {
        return target;
    }
}
