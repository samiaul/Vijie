package com.vijie.core.errors;

public class IndexOutOfRange extends RuntimeException {
    public IndexOutOfRange(int index) {
        super("Index out of range: " + index);
    }
}
