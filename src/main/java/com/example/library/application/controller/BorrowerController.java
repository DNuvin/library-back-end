package com.example.library.application.controller;

import com.example.library.application.dto.BorrowerRequest;
import com.example.library.application.dto.BorrowerResponse;
import com.example.library.domain.service.BorrowerService;
import com.example.library.external.entities.Borrower;
import com.example.library.external.mappers.BorrowerMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/borrowers")
public class BorrowerController {

    private final BorrowerService borrowerService;

    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    // 1. Register a new borrower
    @PostMapping
    public ResponseEntity<BorrowerResponse> registerBorrower(
            @Valid @RequestBody BorrowerRequest request) {
        Borrower borrower = borrowerService.registerBorrower(BorrowerMapper.toEntity(request));
        return ResponseEntity.ok(BorrowerMapper.toResponse(borrower));
    }

    // 2. Borrow a book
    @PostMapping("/{borrowerId}/borrow/{bookId}")
    public ResponseEntity<String> borrowBook(
            @PathVariable Long borrowerId,
            @PathVariable Long bookId) {
        borrowerService.borrowBook(borrowerId, bookId);
        return ResponseEntity.ok("Book borrowed successfully");
    }

    // 3. Return a book
    @PostMapping("/{borrowerId}/return/{bookId}")
    public ResponseEntity<String> returnBook(
            @PathVariable Long borrowerId,
            @PathVariable Long bookId) {
        borrowerService.returnBook(borrowerId, bookId);
        return ResponseEntity.ok("Book returned successfully");
    }
}