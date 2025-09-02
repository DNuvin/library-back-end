package com.example.library.services;

import com.example.library.application.Execeptions.InvalidOperationException;
import com.example.library.domain.service.BorrowerService;
import com.example.library.external.entities.Borrower;
import com.example.library.external.entities.Book;
import com.example.library.external.repository.BorrowerRepositoryInterface;
import com.example.library.external.repository.BookRepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BorrowerServiceTest {

    @Mock
    private BorrowerRepositoryInterface borrowerRepository;

    @Mock
    private BookRepositoryInterface bookRepository;

    @InjectMocks
    private BorrowerService borrowerService;

    private Borrower borrower;
    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        borrower = new Borrower();
        borrower.setId(1L);
        borrower.setName("John Doe");
        borrower.setEmail("john@example.com");

        book = new Book();
        book.setId(1L);
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");
        book.setIsbn("978-1234567890");
    }

    @Test
    void testRegisterBorrowerSuccess() {
        when(borrowerRepository.save(any(Borrower.class))).thenReturn(borrower);

        Borrower result = borrowerService.registerBorrower(borrower);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(borrowerRepository, times(1)).save(borrower);
    }

    @Test
    void testBorrowBookSuccess() {
        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        assertDoesNotThrow(() -> borrowerService.borrowBook(1L, 1L));
        assertEquals(borrower, book.getBorrower());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testBorrowBookAlreadyBorrowed() {
        book.setBorrower(new Borrower());
        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(book));

        assertThrows(InvalidOperationException.class, () -> borrowerService.borrowBook(1L, 1L));
    }

    @Test
    void testReturnBookSuccess() {
        book.setBorrower(borrower);
        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        assertDoesNotThrow(() -> borrowerService.returnBook(1L, 1L));
        assertNull(book.getBorrower());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testReturnBookInvalidBorrower() {
        book.setBorrower(new Borrower());
        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(book));

        assertThrows(InvalidOperationException.class, () -> borrowerService.returnBook(1L, 1L));
    }

    @Test
    void testGetAllBorrowers() {
        when(borrowerRepository.findAll()).thenReturn(List.of(borrower));

        var borrowers = borrowerService.getAllBorrowers();

        assertEquals(1, borrowers.size());
        assertEquals("John Doe", borrowers.get(0).getName());
        verify(borrowerRepository, times(1)).findAll();
    }
}
