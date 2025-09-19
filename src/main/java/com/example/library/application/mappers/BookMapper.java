package com.example.library.application.mappers;

import com.example.library.application.dto.BookRequest;
import com.example.library.application.dto.BookResponse;
import com.example.library.domain.entities.Book;

public class BookMapper {

    public static BookResponse toResponse(Book book) {
        if (book == null) return null;

        Long borrowerId = book.getBorrower() != null ? book.getBorrower().getId() : null;

        return new BookResponse(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                borrowerId
        );
    }

    public static Book toEntity(BookRequest request) {
        if (request == null) return null;

        Book book = new Book();
        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        return book;
    }
}
