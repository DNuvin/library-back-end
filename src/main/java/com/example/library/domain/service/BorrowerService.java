package com.example.library.domain.service;

import com.example.library.application.Execeptions.*;
import com.example.library.external.entities.Borrower;
import com.example.library.external.entities.Book;
import com.example.library.external.repository.BorrowerRepositoryInterface;
import com.example.library.external.repository.BookRepositoryInterface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BorrowerService {

    private final BorrowerRepositoryInterface borrowerRepository;
    private final BookRepositoryInterface bookRepository;

    public BorrowerService(BorrowerRepositoryInterface borrowerRepository,
                           BookRepositoryInterface bookRepository) {
        this.borrowerRepository = borrowerRepository;
        this.bookRepository = bookRepository;
    }

    // Register a new borrower
    public Borrower registerBorrower(Borrower entity) {
        // Optional: validate unique email
        return borrowerRepository.save(entity);
    }

    // Borrow a book
    @Transactional
    public void borrowBook(Long borrowerId, Long bookId) {
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Borrower not found with id: " + borrowerId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Book not found with id: " + bookId));

        if (book.getBorrower() != null) {
            throw new InvalidOperationException(
                    "Book with id " + bookId + " is already borrowed");
        }

        book.setBorrower(borrower);
        bookRepository.save(book); // persist the change
    }

    // Return a book
    @Transactional
    public void returnBook(Long borrowerId, Long bookId) {
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Borrower not found with id: " + borrowerId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Book not found with id: " + bookId));

        if (!borrower.equals(book.getBorrower())) {
            throw new InvalidOperationException(
                    "This borrower did not borrow the book with id: " + bookId);
        }

        book.setBorrower(null);
        bookRepository.save(book);
    }
}
