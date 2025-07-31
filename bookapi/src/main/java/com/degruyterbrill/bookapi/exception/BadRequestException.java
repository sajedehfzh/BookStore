package com.degruyterbrill.bookapi.exception;

/**
 * Custom exception for HTTP 400 Bad Request errors.
 * This is thrown when a client sends a malformed or invalid request.
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
} 