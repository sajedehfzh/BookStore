package com.degruyterbrill.bookapi.exception;

/**
 * Custom exception for HTTP 404 Not Found errors.
 * This is thrown when a specific resource requested by the client does not exist.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
} 