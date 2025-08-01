package com.degruyterbrill.bookapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.degruyterbrill.bookapi.model.Book;

/**
 * Spring Data JPA repository for the {@link Book} entity.
 * It provides standard CRUD operations and also supports JPA specifications for dynamic queries.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    
 boolean existsByIsbn(String isbn);
}
