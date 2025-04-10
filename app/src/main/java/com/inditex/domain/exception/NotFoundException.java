package com.inditex.domain.exception;

/**
 * Exception thrown when a requested resource is not found.
 */
public class NotFoundException extends RuntimeException {

    /**
     * Creates a new NotFoundException with the given message.
     *
     * @param message the detail message
     */
    public NotFoundException(String message) {
        super(message);
    }
}
