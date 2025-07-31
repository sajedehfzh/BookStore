package com.degruyterbrill.bookapi.dto;

import com.degruyterbrill.bookapi.model.BookStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Represents the request payload for creating or updating a book.
 * Contains validation annotations to ensure data integrity.
 */
@Data
public class BookRequest {
    /**
     * The International Standard Book Number (ISBN) of the book.
     * This field is mandatory.
     */
    @NotBlank(message = "ISBN is required")
    private String isbn;

    /**
     * The main title of the book.
     * This field is mandatory.
     */
    @NotBlank(message = "Title is required")
    private String title;

    /**
     * The subtitle of the book, if any. This field is optional.
     */
    private String subtitle;

    /**
     * The copyright year of the book.
     * This field is mandatory.
     */
    @NotBlank(message = "Copyright year is required")
    private String copyrightYear;

    /**
     * The status of the book.
     * This field is mandatory.
     */
    @NotNull(message = "Status is required")
    private BookStatus status;
}
