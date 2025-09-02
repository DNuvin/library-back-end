package com.example.library.external.repository;

import com.example.library.domain.boundary.repositories.BookRepository;
import com.example.library.external.entities.Book;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepositoryInterface extends JpaRepository<Book, Long>, BookRepository {

    // Standard find by ISBN
    List<Book> findByIsbn(String isbn);

    // Implementation of boundary method with pessimistic lock
    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Book b WHERE b.id = :bookId")
    Optional<Book> findByIdForUpdate(Long bookId);
}
