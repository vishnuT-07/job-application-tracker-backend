package com.jobtracker.exception;

/**
 * Custom runtime exception thrown when an entity is not found in the database.
 * CSE Viva Note: Extending RuntimeException allows unchecked exception propagation
 * that GlobalExceptionHandler catches to return HTTP 404 Not Found.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
