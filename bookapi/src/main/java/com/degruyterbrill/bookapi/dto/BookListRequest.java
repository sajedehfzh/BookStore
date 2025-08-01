package com.degruyterbrill.bookapi.dto;
import com.degruyterbrill.bookapi.validator.ValidYearRange;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the query parameters for listing and filtering books.
 * This DTO is used to capture pagination, sorting, and filtering criteria from the request.
 */
@Data
@ValidYearRange
@Getter
@Setter
public class BookListRequest {
    /**
     * The page number to retrieve (0-indexed). Defaults to 0.
     */
    private int page = 0;
    /**
     * The number of items to retrieve per page. Defaults to 10.
     */
    private int size = 10;
    /**
     * The field to sort the results by. Defaults to "isbn".
     */
    private String sort = "isbn";
    /**
     * The sort order ("asc" or "desc"). Defaults to "asc".
     */
    private String order = "asc";

    /**
     * A filter for the book's ISBN (case-insensitive, partial match).
     */
    private String isbn;
    /**
     * A filter for the book's title (case-insensitive, partial match).
     */
    private String title;
    /**
     * A filter for the book's subtitle (case-insensitive, partial match).
     */
    private String subtitle;
    /**
     * A filter for the book's status (e.g., "approved", "pending").
     */
    private String statusFilter;
    /**
     * The starting year for a copyright year range filter.
     */
    private String year_from;
    /**
     * The ending year for a copyright year range filter.
     */
    private String year_to;
}
