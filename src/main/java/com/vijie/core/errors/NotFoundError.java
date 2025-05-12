package com.vijie.core.errors;

import com.vijie.core.interfaces.IParser;

public class NotFoundError extends Exception {

    private final IParser<?> target;

    public NotFoundError(IParser<?> target) {
        super("Target not found: " + target.toString());
        this.target = target;
    }

    public IParser<?> getTarget() {
        return target;
    }
}
