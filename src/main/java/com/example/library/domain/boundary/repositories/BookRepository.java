package com.example.library.domain.boundary.repositories;

import com.example.library.domain.entities.Book;

import java.util.Optional;

public interface BookRepository {

    // Fetch book with pessimistic lock to prevent double borrowing
    Optional<Book> findByIdForUpdate(Long bookId);
}
