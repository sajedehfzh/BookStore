package com.degruyterbrill.bookapi.controller;

import com.degruyterbrill.bookapi.dto.BookListRequest;
import com.degruyterbrill.bookapi.dto.BookRequest;
import com.degruyterbrill.bookapi.dto.BookUpdateRequest;
import com.degruyterbrill.bookapi.model.Book;
import com.degruyterbrill.bookapi.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * REST controller for managing books.
 * Provides endpoints for creating, retrieving, and listing books.
 */
@RestController
@RequestMapping("/api/books")
@Validated
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * GET /api/books : Get a paginated and filtered list of all books.
     *
     * @param request The request object containing pagination, sorting, and filtering parameters.
     * @return a {@link Page} of {@link Book} objects.
     */
    @GetMapping
    public Page<Book> getAllBooks(@Valid BookListRequest request) {
        return bookService.getAllBooks(request);
    }

    /**
     * GET /api/books/{id} : Get the "id" book.
     *
     * @param id the id of the book to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the book, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    /**
     * POST /api/books : Create a new book.
     *
     * @param bookRequest the book to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new book.
     */
    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody BookRequest bookRequest) {
        Book createdBook = bookService.createBook(bookRequest);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id,
                                           @RequestBody BookUpdateRequest book)
    {
        Book newBook = bookService.updateBook(id, book);
        return ResponseEntity.ok(newBook);
    }

}
