package com.example.library.services;

import com.example.library.application.dto.BookResponse;
import com.example.library.domain.service.BookService;
import com.example.library.domain.entities.Book;
import com.example.library.external.repository.BookRepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepositoryInterface bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterBook_NewBook() {
        Book book = new Book();
        book.setId(1L);
        book.setIsbn("1234567890");
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");

        when(bookRepository.findByIsbn("1234567890")).thenReturn(List.of());
        when(bookRepository.save(book)).thenReturn(book);

        BookResponse response = bookService.registerBook(book);

        assertNotNull(response);
        assertEquals("Effective Java", response.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testRegisterBook_DuplicateSameDetails() {
        Book existing = new Book();
        existing.setId(1L);
        existing.setIsbn("1234567890");
        existing.setTitle("Effective Java");
        existing.setAuthor("Joshua Bloch");

        Book newBook = new Book();
        newBook.setIsbn("1234567890");
        newBook.setTitle("Effective Java");
        newBook.setAuthor("Joshua Bloch");

        when(bookRepository.findByIsbn("1234567890")).thenReturn(List.of(existing));

        BookResponse response = bookService.registerBook(newBook);

        assertNotNull(response);
        assertEquals(existing.getId(), response.getId());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void testRegisterBook_DuplicateDifferentDetails() {
        Book existing = new Book();
        existing.setId(1L);
        existing.setIsbn("1234567890");
        existing.setTitle("Effective Java");
        existing.setAuthor("Joshua Bloch");

        Book newBook = new Book();
        newBook.setIsbn("1234567890");
        newBook.setTitle("Java Concurrency");
        newBook.setAuthor("Brian Goetz");

        when(bookRepository.findByIsbn("1234567890")).thenReturn(List.of(existing));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookService.registerBook(newBook));

        assertTrue(exception.getMessage().contains("same ISBN must have the same title and author"));
        verify(bookRepository, never()).save(any());
    }

    @Test
    void testGetAllBooks() {
        Book b1 = new Book();
        b1.setId(1L);
        b1.setTitle("Effective Java");
        b1.setIsbn("1234567890");
        b1.setAuthor("Joshua Bloch");

        Book b2 = new Book();
        b2.setId(2L);
        b2.setTitle("Clean Code");
        b2.setIsbn("0987654321");
        b2.setAuthor("Robert C. Martin");

        when(bookRepository.findAll()).thenReturn(List.of(b1, b2));

        List<BookResponse> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        assertEquals("Effective Java", result.get(0).getTitle());
        assertEquals("Clean Code", result.get(1).getTitle());
    }

    @Test
    void testGetBookById_Success() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Effective Java");
        book.setIsbn("1234567890");
        book.setAuthor("Joshua Bloch");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookResponse response = bookService.getBookById(1L);

        assertNotNull(response);
        assertEquals("Effective Java", response.getTitle());
    }

    @Test
    void testGetBookById_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> bookService.getBookById(1L));

        assertTrue(exception.getMessage().contains("Book not found with ID"));
    }
}
