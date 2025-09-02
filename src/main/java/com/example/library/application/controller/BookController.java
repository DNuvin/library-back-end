package com.example.library.application.controller;

import com.example.library.application.dto.BookRequest;
import com.example.library.application.dto.BookResponse;
import com.example.library.domain.service.BookService;
import com.example.library.external.entities.Book;
import com.example.library.external.mappers.BookMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Slf4j
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Register a new book
     */
    @PostMapping
    public ResponseEntity<BookResponse> registerBook(@Valid @RequestBody BookRequest request) {
        log.info("Received request to register book: {}", request);
        BookResponse response = bookService.registerBook(BookMapper.toEntity(request));
        log.info("Book registered successfully with ID: {}", response.getId());
        return ResponseEntity.ok(response);
    }

    /**
     * Get all registered books
     */
    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        log.info("Received request to fetch all books");
        List<BookResponse> response = bookService.getAllBooks();
        log.info("Returning {} books", response.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Get a book by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        log.info("Received request to fetch book by ID: {}", id);
        BookResponse response = bookService.getBookById(id);
        log.info("Returning book: {}", response.getTitle());
        return ResponseEntity.ok(response);
    }
}
