package com.degruyterbrill.bookapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents a book entity in the database.
 */
@Entity
@Table(name = "books")
public class Book {

    /**
     * The unique identifier for the book.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The International Standard Book Number (ISBN) of the book. Must be unique.
     */
    @Column(unique = true, nullable = false)
    private String isbn;

    /**
     * The main title of the book.
     */
    @Column(nullable = false)
    private String title;

    /**
     * The subtitle of the book, if any.
     */
    private String subtitle;

    /**
     * The copyright year of the book.
     */
    @Column(nullable = false)
    private String copyrightYear;

    /**
     * The current status of the book in the workflow (e.g., PENDING, APPROVED).
     */
    @Column(nullable = false)
    private BookStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getCopyrightYear() {
        return copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear) {
        this.copyrightYear = copyrightYear;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }
}
