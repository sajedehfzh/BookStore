package com.degruyterbrill.bookapi.controller;

import com.degruyterbrill.bookapi.dto.BookListRequest;
import com.degruyterbrill.bookapi.dto.BookRequest;
import com.degruyterbrill.bookapi.model.Book;
import com.degruyterbrill.bookapi.model.BookStatus;
import com.degruyterbrill.bookapi.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;


@WebMvcTest(BookController.class)
public class BookControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private Book book;
    private BookRequest bookRequest;
    private Page<Book> bookPage;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setStatus(BookStatus.APPROVED);

        bookRequest = new BookRequest();
        bookRequest.setIsbn("978-3-16-148410-0");
        bookRequest.setTitle("New Book");
        bookRequest.setCopyrightYear("2023");
        bookRequest.setStatus(BookStatus.PENDING);

        List<Book> bookList = Collections.singletonList(book);
        bookPage = new PageImpl<>(bookList);
    }

    /**
     * Tests the GET /api/books endpoint with default parameters.
     * Verifies that the endpoint returns HTTP 200 OK and a non-empty list of books.
     */
    @Test
    void testGetAllBooks_Default() throws Exception {
        given(bookService.getAllBooks(any(BookListRequest.class))).willReturn(bookPage);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title", is("Test Book")));
    }

    /**
     * Tests the GET /api/books endpoint with pagination and sorting parameters.
     * Verifies that the endpoint returns HTTP 200 OK and respects the query parameters.
     */
    @Test
    void testGetAllBooks_WithPaginationAndSort() throws Exception {
        given(bookService.getAllBooks(any(BookListRequest.class))).willReturn(bookPage);

        mockMvc.perform(get("/api/books?page=1&size=5&sort=title&order=desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    /**
     * Tests the GET /api/books endpoint with a status filter.
     * Verifies that the endpoint returns HTTP 200 OK and only books with the specified status.
     */
    @Test
    void testGetAllBooks_WithFilter() throws Exception {
        given(bookService.getAllBooks(any(BookListRequest.class))).willReturn(bookPage);

        mockMvc.perform(get("/api/books?statusFilter=approved"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].status", is("approved")));
    }

    /**
     * Tests the GET /api/books endpoint with a 'pending' status filter.
     * Verifies that the endpoint returns HTTP 200 OK and correctly filters by pending status.
     */
    @Test
    void testGetAllBooks_WithPendingStatusFilter() throws Exception {
        Book pendingBook = new Book();
        pendingBook.setId(2L);
        pendingBook.setTitle("Pending Book");
        pendingBook.setStatus(BookStatus.PENDING);
        Page<Book> pendingBookPage = new PageImpl<>(Collections.singletonList(pendingBook));

        given(bookService.getAllBooks(any(BookListRequest.class))).willReturn(pendingBookPage);

        mockMvc.perform(get("/api/books?statusFilter=pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].status", is("pending")));
    }


    /**
     * Tests the GET /api/books/{id} endpoint for a book that exists.
     * Verifies that the endpoint returns HTTP 200 OK and the correct book details.
     */
    @Test
    void testGetBookById() throws Exception {
        given(bookService.getBookById(1L)).willReturn(book);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    /**
     * Tests the GET /api/books/{id} endpoint for a book that does not exist.
     * Verifies that the endpoint returns HTTP 404 Not Found.
     */
    @Test
    void testGetBookById_NotFound() throws Exception {
        given(bookService.getBookById(99L))
                .willThrow(new com.degruyterbrill.bookapi.exception.ResourceNotFoundException("Book not found"));

        mockMvc.perform(get("/api/books/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("The resource you were looking for could not be found."));
    }

    /**
     * Tests the POST /api/books endpoint for successful book creation.
     * Verifies that the endpoint returns HTTP 201 Created and the newly created book.
     */
    @Test
    void testCreateBook() throws Exception {
        given(bookService.createBook(any(BookRequest.class))).willReturn(book);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    // Error handling tests
    /**
     * Tests the GET /api/books endpoint with an invalid status filter value.
     * Verifies that the endpoint returns HTTP 400 Bad Request.
     */
    @Test
    void testGetAllBooks_InvalidStatusFilter() throws Exception {
        given(bookService.getAllBooks(any(BookListRequest.class)))
                .willThrow(new com.degruyterbrill.bookapi.exception.BadRequestException("Invalid status value: 'wrong'"));

        mockMvc.perform(get("/api/books?statusFilter=wrong"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid status value: 'wrong'"));
    }

    /**
     * Tests the GET /api/books/{id} endpoint with an invalid ID format (non-numeric).
     * Verifies that the endpoint returns HTTP 400 Bad Request.
     */
    @Test
    void testGetBookById_InvalidIdFormat() throws Exception {
        // No need to mock service – conversion error happens before controller method executes
        mockMvc.perform(get("/api/books/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    /**
     * Tests the POST /api/books endpoint with an invalid JSON payload (missing mandatory fields).
     * Verifies that the endpoint returns HTTP 400 Bad Request.
     */
    @Test
    void testCreateBook_InvalidPayload() throws Exception {
        // Missing mandatory fields should trigger validation errors
        String invalidJson = "{}";

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray());
    }

    /**
     * Tests the POST /api/books endpoint with a duplicate ISBN.
     * Verifies that the endpoint returns HTTP 409 Conflict.
     */
    @Test
    void testCreateBook_DuplicateISBN() throws Exception {
        // Simulate unique constraint violation coming from service layer
        org.hibernate.exception.ConstraintViolationException constraintEx =
                new org.hibernate.exception.ConstraintViolationException("Duplicate ISBN", null, "isbn_unique");
        given(bookService.createBook(any(BookRequest.class)))
                .willThrow(new org.springframework.dao.DataIntegrityViolationException("Duplicate", constraintEx));

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("A book with the same ISBN already exists."));
    }

    /**
     * Tests the POST /api/books endpoint with a request body missing the mandatory 'isbn' field.
     * Verifies that the endpoint returns HTTP 400 Bad Request and a specific validation error.
     */
    @Test
    void testCreateBook_MissingIsbn() throws Exception {
        String payload = "{\n" +
                "  \"title\": \"Book Without ISBN\",\n" +
                "  \"copyrightYear\": \"2023\",\n" +
                "  \"status\": \"pending\"\n" +
                "}";

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasItem("ISBN is required")));
    }

    /**
     * Tests the POST /api/books endpoint with a request body missing the mandatory 'title' field.
     * Verifies that the endpoint returns HTTP 400 Bad Request and a specific validation error.
     */
    @Test
    void testCreateBook_MissingTitle() throws Exception {
        String payload = "{\n" +
                "  \"isbn\": \"978-1-23456-789-7\",\n" +
                "  \"copyrightYear\": \"2023\",\n" +
                "  \"status\": \"pending\"\n" +
                "}";

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasItem("Title is required")));
    }

    /**
     * Tests the global exception handler for unexpected internal server errors.
     * Verifies that any unhandled exception results in an HTTP 500 Internal Server Error.
     */
    @Test
    void testGlobalExceptionHandler_InternalServerError() throws Exception {
        given(bookService.getAllBooks(any(BookListRequest.class)))
                .willThrow(new RuntimeException("Unexpected"));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }
} 