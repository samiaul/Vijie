package com.vijie.core.errors;

/**
 * Exception thrown when an illegal extent range is encountered.
 */
public class IllegalExtentRangeException extends RuntimeException {

    /**
     * Constructs a new IllegalExtentRangeException with the specified detail message.
     *
     * @param extentMin the minimum extent
     * @param extentMax the maximum extent
     */
    public IllegalExtentRangeException(int extentMin, int extentMax) {
        super("Illegal extent range: [%d, %d]".formatted(extentMin, extentMax));
    }
}