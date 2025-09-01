package com.example.library.application.controller;

import com.example.library.application.dto.BookRequest;
import com.example.library.application.dto.BookResponse;
import com.example.library.domain.service.BookService;
import com.example.library.external.entities.Book;
import com.example.library.external.mappers.BookMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookResponse> registerBook(@Valid @RequestBody BookRequest request) {
        Book book = bookService.registerBook(BookMapper.toEntity(request));
        return ResponseEntity.ok(BookMapper.toResponse(book));
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        List<BookResponse> response = bookService.getAllBooks(); // already DTOs
        return ResponseEntity.ok(response);
    }

}

