package com.degruyterbrill.bookapi.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the status of a book in the workflow.
 */
public enum BookStatus {
    /**
     * The book has been approved.
     */
    APPROVED("approved"),

    /**
     * The book is pending review.
     */
    PENDING("pending"),

    /**
     * The book has been rejected.
     */
    REJECTED("rejected");

    private final String value;

    BookStatus(String value) {
        this.value = value;
    }

    /**
     * Gets the string value of the enum, used for JSON serialization.
     * @return The lower-case string representation of the status.
     */
    @JsonValue
    public String getValue() {
        return value;
    }

    /**
     * Creates a BookStatus enum from a string value, used for JSON deserialization.
     * This method is case-insensitive.
     * @param value The string value to convert.
     * @return The corresponding BookStatus enum.
     * @throws IllegalArgumentException if the value does not match any known status.
     */
    @JsonCreator
    public static BookStatus fromValue(String value) {
        for (BookStatus status : BookStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown enum type " + value);
    }
} 