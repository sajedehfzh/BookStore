package com.degruyterbrill.bookapi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.degruyterbrill.bookapi.dto.BookUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.function.EntityResponse;

import com.degruyterbrill.bookapi.dto.BookListRequest;
import com.degruyterbrill.bookapi.dto.BookRequest;
import com.degruyterbrill.bookapi.exception.BadRequestException;
import com.degruyterbrill.bookapi.exception.ResourceNotFoundException;
import com.degruyterbrill.bookapi.model.Book;
import com.degruyterbrill.bookapi.model.BookStatus;
import com.degruyterbrill.bookapi.repository.BookRepository;

import jakarta.persistence.criteria.Predicate;

/**
 * Service class for managing books.
 * This class contains the business logic for book-related operations.
 */
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    /**
     * Retrieves a paginated and filtered list of books based on the provided request parameters.
     *
     * @param request The request object containing pagination, sorting, and filtering criteria.
     * @return A {@link Page} of {@link Book} objects matching the criteria.
     * @throws BadRequestException if the status filter value is invalid.
     */
    public Page<Book> getAllBooks(BookListRequest request) {
        Sort sort = Sort.by(Sort.Direction.fromString(request.getOrder()), request.getSort());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Specification<Book> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(request.getIsbn())) {
                predicates.add(cb.like(cb.lower(root.get("isbn")), "%" + request.getIsbn().toLowerCase() + "%"));
            }
            if (StringUtils.hasText(request.getTitle())) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + request.getTitle().toLowerCase() + "%"));
            }
            if (StringUtils.hasText(request.getSubtitle())) {
                predicates.add(cb.like(cb.lower(root.get("subtitle")), "%" + request.getSubtitle().toLowerCase() + "%"));
            }
            if (StringUtils.hasText(request.getStatusFilter())) {
                try {
                    BookStatus status = BookStatus.fromValue(request.getStatusFilter());
                    predicates.add(cb.equal(cb.lower(root.get("status").as(String.class)), status.getValue().toLowerCase()));
                } catch (IllegalArgumentException e) {
                    throw new BadRequestException("Invalid status value: '" + request.getStatusFilter() + "'. Allowed values are: " +
                            Arrays.stream(BookStatus.values()).map(BookStatus::getValue).collect(Collectors.joining(", ")));
                }
            }
            if (StringUtils.hasText(request.getYear_from())) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("copyrightYear"), request.getYear_from()));
            }
            if (StringUtils.hasText(request.getYear_to())) {
                predicates.add(cb.lessThanOrEqualTo(root.get("copyrightYear"), request.getYear_to()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return bookRepository.findAll(spec, pageable);
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param id The ID of the book to retrieve.
     * @return The {@link Book} object.
     * @throws ResourceNotFoundException if no book is found with the given ID.
     */
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    /**
     * Creates a new book from the given book request data.
     *
     * @param bookRequest The request object containing the details of the book to create.
     * @return The newly created {@link Book} object.
     */
    public Book createBook(BookRequest bookRequest) {
        Book book = new Book();
        book.setIsbn(bookRequest.getIsbn());
        book.setTitle(bookRequest.getTitle());
        book.setSubtitle(bookRequest.getSubtitle());
        book.setCopyrightYear(bookRequest.getCopyrightYear());
        book.setStatus(bookRequest.getStatus());
        return bookRepository.save(book);
    }

    public void deleteBook(Long id){
       Book book= bookRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("book that you want ot delete not exist " + id));
       bookRepository.delete(book);
    }

    public Book updateBook(Long id, BookUpdateRequest newBook){
       Book book= bookRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("newBook that you want ot delete not exist " + id));
       if(newBook.getCopyrightYear()!=null)
       { book.setCopyrightYear(newBook.getCopyrightYear()); }
       if(newBook.getIsbn()!=null)
       {book.setIsbn(newBook.getIsbn());}
        if(newBook.getStatus()!=null){
       book.setStatus(newBook.getStatus());}
        if(newBook.getTitle()!=null)
        {book.setTitle(newBook.getTitle());}
        if(newBook.getTitle()!=null){
       book.setSubtitle(newBook.getSubtitle());}
       return bookRepository.save(book);

    }

   
}
