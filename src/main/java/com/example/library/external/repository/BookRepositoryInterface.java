package com.example.library.external.repository;

import com.example.library.domain.boundary.repositories.BookRepository;
import com.example.library.external.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepositoryInterface extends JpaRepository<Book , Long>, BookRepository {
}
