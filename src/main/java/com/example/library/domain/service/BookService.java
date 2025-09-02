package com.example.library.domain.service;

import com.example.library.application.dto.BookResponse;
import com.example.library.external.entities.Book;
import com.example.library.external.mappers.BookMapper;
import com.example.library.external.repository.BookRepositoryInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class BookService {

    private final BookRepositoryInterface bookRepository;

    public BookService(BookRepositoryInterface bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Register a book and return BookResponse DTO
     */
    @Transactional
    public BookResponse registerBook(Book book) {
        log.info("Attempting to register book with ISBN: {}", book.getIsbn());

        List<Book> sameIsbnBooks = bookRepository.findByIsbn(book.getIsbn());
        if (!sameIsbnBooks.isEmpty()) {
            Book existing = sameIsbnBooks.get(0);
            if (!existing.getTitle().equals(book.getTitle()) ||
                    !existing.getAuthor().equals(book.getAuthor())) {
                throw new IllegalArgumentException(
                        "Books with the same ISBN must have the same title and author");
            }
            log.info("Book with ISBN {} already exists, returning existing record", book.getIsbn());
            return BookMapper.toResponse(existing);
        }

        Book saved = bookRepository.save(book);
        log.info("Book registered successfully with ID: {}", saved.getId());
        return BookMapper.toResponse(saved);
    }

    /**
     * Get all books as DTOs
     */
    public List<BookResponse> getAllBooks() {
        log.info("Fetching all books from repository");
        return bookRepository.findAll()
                .stream()
                .map(BookMapper::toResponse)
                .toList();
    }

    /**
     * Fetch a book by its ID
     */
    public BookResponse getBookById(Long id) {
        log.info("Fetching book with ID: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Book with ID {} not found", id);
                    return new NoSuchElementException("Book not found with ID: " + id);
                });
        log.info("Book fetched successfully: {}", book.getTitle());
        return BookMapper.toResponse(book);
    }
}
