package com.app.exception;

/**
 * Exception thrown when an attempt is made to create a resource that already exists.
 */
public class ResourceAlreadyExistException extends RuntimeException {

    /**
     * Constructs a new ResourceAlreadyExistException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception.
     */
    public ResourceAlreadyExistException(String message) {
        super(message);
    }
}
