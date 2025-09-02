package com.example.library.domain.service;

import com.example.library.application.Execeptions.InvalidOperationException;
import com.example.library.application.Execeptions.ResourceNotFoundException;
import com.example.library.external.entities.Borrower;
import com.example.library.external.entities.Book;
import com.example.library.external.mappers.BorrowerMapper;
import com.example.library.external.repository.BorrowerRepositoryInterface;
import com.example.library.external.repository.BookRepositoryInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class BorrowerService {

    private final BorrowerRepositoryInterface borrowerRepository;
    private final BookRepositoryInterface bookRepository;

    public BorrowerService(BorrowerRepositoryInterface borrowerRepository,
                           BookRepositoryInterface bookRepository) {
        this.borrowerRepository = borrowerRepository;
        this.bookRepository = bookRepository;
    }

    // Register a new borrower and return entity
    public Borrower registerBorrower(Borrower entity) {
        log.info("Registering borrower: {}", entity);
        return borrowerRepository.save(entity);
    }

    // Borrow a book with row locking to prevent conflicts
    @Transactional
    public void borrowBook(Long borrowerId, Long bookId) {
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Borrower not found with id: " + borrowerId));

        Book book = bookRepository.findByIdForUpdate(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Book not found with id: " + bookId));

        if (book.getBorrower() != null) {
            throw new InvalidOperationException(
                    "Book with id " + bookId + " is already borrowed");
        }

        book.setBorrower(borrower);
        bookRepository.save(book);
        log.info("Book {} successfully borrowed by borrower {}", bookId, borrowerId);
    }

    // Return a borrowed book
    @Transactional
    public void returnBook(Long borrowerId, Long bookId) {
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Borrower not found with id: " + borrowerId));

        Book book = bookRepository.findByIdForUpdate(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Book not found with id: " + bookId));

        if (!borrower.equals(book.getBorrower())) {
            throw new InvalidOperationException(
                    "This borrower did not borrow the book with id: " + bookId);
        }

        book.setBorrower(null);
        bookRepository.save(book);
        log.info("Book {} successfully returned by borrower {}", bookId, borrowerId);
    }

    // Get all borrowers as DTOs
    public List<com.example.library.application.dto.BorrowerResponse> getAllBorrowers() {
        log.info("Fetching all borrowers from database");
        return borrowerRepository.findAll()
                .stream()
                .map(BorrowerMapper::toResponse)
                .toList();
    }
}
