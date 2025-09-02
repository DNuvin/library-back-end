package com.example.library.application.controller;

import com.example.library.application.dto.BorrowerRequest;
import com.example.library.application.dto.BorrowerResponse;
import com.example.library.domain.service.BorrowerService;
import com.example.library.external.entities.Borrower;
import com.example.library.external.mappers.BorrowerMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowers")
@Slf4j
public class BorrowerController {

    private final BorrowerService borrowerService;

    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    // 1. Register a new borrower
    @PostMapping
    public ResponseEntity<BorrowerResponse> registerBorrower(
            @Valid @RequestBody BorrowerRequest request) {
        log.info("Received request to register borrower: {}", request);
        Borrower borrower = borrowerService.registerBorrower(BorrowerMapper.toEntity(request));
        BorrowerResponse response = BorrowerMapper.toResponse(borrower);
        log.info("Borrower registered successfully with ID: {}", response.getId());
        return ResponseEntity.ok(response);
    }

    // 2. Borrow a book
    @PostMapping("/{borrowerId}/borrow/{bookId}")
    public ResponseEntity<String> borrowBook(
            @PathVariable Long borrowerId,
            @PathVariable Long bookId) {
        log.info("Borrower {} is attempting to borrow book {}", borrowerId, bookId);
        borrowerService.borrowBook(borrowerId, bookId);
        log.info("Borrower {} successfully borrowed book {}", borrowerId, bookId);
        return ResponseEntity.ok("Book borrowed successfully");
    }

    // 3. Return a book
    @PostMapping("/{borrowerId}/return/{bookId}")
    public ResponseEntity<String> returnBook(
            @PathVariable Long borrowerId,
            @PathVariable Long bookId) {
        log.info("Borrower {} is attempting to return book {}", borrowerId, bookId);
        borrowerService.returnBook(borrowerId, bookId);
        log.info("Borrower {} successfully returned book {}", borrowerId, bookId);
        return ResponseEntity.ok("Book returned successfully");
    }

    // 4. Get all borrowers
    @GetMapping
    public ResponseEntity<List<BorrowerResponse>> getAllBorrowers() {
        log.info("Fetching all borrowers");
        List<BorrowerResponse> borrowers = borrowerService.getAllBorrowers();
        log.info("Returning {} borrowers", borrowers.size());
        return ResponseEntity.ok(borrowers);
    }
}
