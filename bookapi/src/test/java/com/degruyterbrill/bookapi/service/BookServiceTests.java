package com.degruyterbrill.bookapi.service;

import com.degruyterbrill.bookapi.dto.BookListRequest;
import com.degruyterbrill.bookapi.dto.BookRequest;
import com.degruyterbrill.bookapi.exception.ResourceNotFoundException;
import com.degruyterbrill.bookapi.model.Book;
import com.degruyterbrill.bookapi.model.BookStatus;
import com.degruyterbrill.bookapi.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.mockito.ArgumentMatchers;


import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private BookRequest bookRequest;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setIsbn("1234567890");
        book.setStatus(BookStatus.APPROVED);
        book.setCopyrightYear("2021");

        bookRequest = new BookRequest();
        bookRequest.setTitle("New Book");
        bookRequest.setIsbn("0987654321");
        bookRequest.setCopyrightYear("2023");
        bookRequest.setStatus(BookStatus.PENDING);
    }

    /**
     * Tests the successful retrieval of a paginated list of books.
     * Verifies that the repository's findAll method is called and returns a non-empty page.
     */
    @Test
    void testGetAllBooks_Success() {
        BookListRequest bookListRequest = new BookListRequest();
        Page<Book> bookPage = new PageImpl<>(Collections.singletonList(book));
        when(bookRepository.findAll(ArgumentMatchers.<Specification<Book>>any(), any(Pageable.class))).thenReturn(bookPage);

        Page<Book> result = bookService.getAllBooks(bookListRequest);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Book", result.getContent().get(0).getTitle());
    }

    /**
     * Tests finding a book by its ID when the book exists.
     * Verifies that the service returns the correct book.
     */
    @Test
    void testGetBookById_Found() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Book foundBook = bookService.getBookById(1L);
        assertNotNull(foundBook);
        assertEquals("Test Book", foundBook.getTitle());
    }

    /**
     * Tests finding a book by its ID when the book does not exist.
     * Verifies that a ResourceNotFoundException is thrown.
     */
    @Test
    void testGetBookById_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(1L));
    }

    /**
     * Tests the successful creation of a new book.
     * Verifies that the repository's save method is called and a new book entity is returned with an ID.
     */
    @Test
    void testCreateBook() {
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book savedBook = invocation.getArgument(0);
            savedBook.setId(2L);
            return savedBook;
        });

        Book createdBook = bookService.createBook(bookRequest);

        assertNotNull(createdBook);
        assertEquals("New Book", createdBook.getTitle());
        assertNotNull(createdBook.getId());
    }

    /**
     * Tests that creating a book with a duplicate ISBN throws a DataIntegrityViolationException.
     * Verifies that the service correctly propagates the exception from the data layer.
     */
    @Test
    void testCreateBook_DuplicateISBN_ThrowsDataIntegrityViolation() {
        when(bookRepository.save(any(Book.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThrows(DataIntegrityViolationException.class, () -> bookService.createBook(bookRequest));
    }
} 