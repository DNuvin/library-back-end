package com.example.library.application.controller;

import com.example.library.application.dto.BorrowerRequest;
import com.example.library.application.dto.BorrowerResponse;
import com.example.library.domain.service.BorrowerService;
import com.example.library.domain.entities.Borrower;
import com.example.library.external.mappers.BorrowerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowers")
@Slf4j
@Tag(name = "Borrower APIs", description = "APIs for managing library borrowers")
public class BorrowerController {

    private final BorrowerService borrowerService;

    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    /**
     * Register a new borrower
     */
    @PostMapping
    @Operation(summary = "Register Borrower", description = "Registers a new borrower in the library")
    public ResponseEntity<BorrowerResponse> registerBorrower(
            @Valid @RequestBody BorrowerRequest request) {
        log.info("Received request to register borrower: {}", request);
        Borrower borrower = borrowerService.registerBorrower(BorrowerMapper.toEntity(request));
        BorrowerResponse response = BorrowerMapper.toResponse(borrower);
        log.info("Borrower registered successfully with ID: {}", response.getId());
        return ResponseEntity.ok(response);
    }

    /**
     * Borrow a book
     */
    @PostMapping("/{borrowerId}/borrow/{bookId}")
    @Operation(summary = "Borrow Book", description = "Allows a borrower to borrow a book by ID")
    public ResponseEntity<String> borrowBook(
            @Parameter(description = "ID of the borrower") @PathVariable Long borrowerId,
            @Parameter(description = "ID of the book to borrow") @PathVariable Long bookId) {
        log.info("Borrower {} is attempting to borrow book {}", borrowerId, bookId);
        borrowerService.borrowBook(borrowerId, bookId);
        log.info("Borrower {} successfully borrowed book {}", borrowerId, bookId);
        return ResponseEntity.ok("Book borrowed successfully");
    }

    /**
     * Return a borrowed book
     */
    @PostMapping("/{borrowerId}/return/{bookId}")
    @Operation(summary = "Return Book", description = "Allows a borrower to return a borrowed book by ID")
    public ResponseEntity<String> returnBook(
            @Parameter(description = "ID of the borrower") @PathVariable Long borrowerId,
            @Parameter(description = "ID of the book to return") @PathVariable Long bookId) {
        log.info("Borrower {} is attempting to return book {}", borrowerId, bookId);
        borrowerService.returnBook(borrowerId, bookId);
        log.info("Borrower {} successfully returned book {}", borrowerId, bookId);
        return ResponseEntity.ok("Book returned successfully");
    }

    /**
     * Get all borrowers
     */
    @GetMapping
    @Operation(summary = "Get All Borrowers", description = "Fetches a list of all registered borrowers")
    public ResponseEntity<List<BorrowerResponse>> getAllBorrowers() {
        log.info("Fetching all borrowers");
        List<BorrowerResponse> borrowers = borrowerService.getAllBorrowers();
        log.info("Returning {} borrowers", borrowers.size());
        return ResponseEntity.ok(borrowers);
    }

    /**
     * Get borrower by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get Borrower By ID", description = "Fetches a borrower's details by their ID")
    public ResponseEntity<BorrowerResponse> getBorrowerById(
            @Parameter(description = "ID of the borrower") @PathVariable Long id) {
        log.info("Received request to fetch borrower by ID: {}", id);
        BorrowerResponse response = borrowerService.getBorrowerById(id);
        log.info("Returning borrower: {}", response.getName());
        return ResponseEntity.ok(response);
    }
}
