package com.degruyterbrill.bookapi.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a standardized error response body for API errors.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    /**
     * The HTTP status code.
     */
    private int statusCode;
    /**
     * A user-friendly message explaining the error.
     */
    private String message;
    /**
     * The timestamp when the error occurred.
     */
    private LocalDateTime timestamp;
    /**
     * A list of specific validation errors, if applicable.
     */
    private List<String> errors;

    /**
     * Constructor for creating an error response without a list of specific errors.
     * @param statusCode The HTTP status code.
     * @param message A user-friendly message.
     * @param timestamp The time of the error.
     */
    public ErrorResponse(int statusCode, String message, LocalDateTime timestamp) {
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = timestamp;
    }

    /**
     * Constructor for creating an error response that includes a list of specific errors (e.g., validation failures).
     * @param statusCode The HTTP status code.
     * @param message A user-friendly message.
     * @param timestamp The time of the error.
     * @param errors A list of detailed error messages.
     */
    public ErrorResponse(int statusCode, String message, LocalDateTime timestamp, List<String> errors) {
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = timestamp;
        this.errors = errors;
    }
} 