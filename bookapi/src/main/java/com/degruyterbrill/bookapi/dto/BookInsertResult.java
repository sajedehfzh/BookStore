package com.degruyterbrill.bookapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents the result of a book insertion operation.
 */
@Data
@AllArgsConstructor
public class BookInsertResult {
    /**
     * A message indicating the outcome of the insertion.
     */
    private String message;
}
