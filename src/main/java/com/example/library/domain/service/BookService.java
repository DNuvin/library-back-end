package com.example.library.domain.service;

import com.example.library.application.dto.BookRequest;
import com.example.library.application.dto.BookResponse;
import com.example.library.external.entities.Book;
import com.example.library.external.repository.BookRepositoryInterface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    private final BookRepositoryInterface bookRepository;

    public BookService(BookRepositoryInterface bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Add a book and return BookResponse DTO
    @Transactional
    public BookResponse addBook(Book book) {
        // Optional: check ISBN consistency (same ISBN must have same title/author)
        List<Book> sameIsbnBooks = bookRepository.findAll().stream()
                .filter(b -> b.getIsbn().equals(book.getIsbn()))
                .toList();

        if (!sameIsbnBooks.isEmpty()) {
            Book existing = sameIsbnBooks.get(0);
            if (!existing.getTitle().equals(book.getTitle()) || !existing.getAuthor().equals(book.getAuthor())) {
                throw new IllegalArgumentException("Books with same ISBN must have same title and author");
            }
        }

        Book saved = bookRepository.save(book);
        return toResponse(saved);
    }

    // Get all books as DTOs
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Register book (returns entity)
    public Book registerBook(Book entity) {
        return bookRepository.save(entity);
    }

    // Mapper method
    private BookResponse toResponse(Book book) {
        Long borrowerId = book.getBorrower() != null ? book.getBorrower().getId() : null;
        return new BookResponse(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                borrowerId
        );
    }
}
