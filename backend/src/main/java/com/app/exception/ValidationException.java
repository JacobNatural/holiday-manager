package com.app.exception;

/**
 * Exception thrown when validation of an input or data fails.
 */
public class ValidationException extends RuntimeException {

    /**
     * Constructs a new ValidationException with the specified detail message.
     *
     * @param message the detail message explaining the validation failure.
     */
    public ValidationException(String message) {
        super(message);
    }
}
