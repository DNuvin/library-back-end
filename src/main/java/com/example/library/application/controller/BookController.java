package com.example.library.application.controller;

import com.example.library.application.dto.BookRequest;
import com.example.library.application.dto.BookResponse;
import com.example.library.domain.service.BookService;
import com.example.library.application.mappers.BookMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Slf4j
@Tag(name = "Book APIs", description = "APIs for managing books in the library")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Register a new book
     */
    @PostMapping
    @Operation(summary = "Register Book", description = "Registers a new book in the library")
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
    @Operation(summary = "Get All Books", description = "Fetches a list of all registered books")
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
    @Operation(summary = "Get Book By ID", description = "Fetches a book's details by its ID")
    public ResponseEntity<BookResponse> getBookById(
            @Parameter(description = "ID of the book") @PathVariable Long id) {
        log.info("Received request to fetch book by ID: {}", id);
        BookResponse response = bookService.getBookById(id);
        log.info("Returning book: {}", response.getTitle());
        return ResponseEntity.ok(response);
    }
}
