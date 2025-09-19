package com.example.library.application.mappers;

import com.example.library.application.dto.BorrowerRequest;
import com.example.library.application.dto.BorrowerResponse;
import com.example.library.domain.entities.Borrower;


import java.util.stream.Collectors;

public class BorrowerMapper {

    public static BorrowerResponse toResponse(Borrower borrower) {
        if (borrower == null) return null;

        return new BorrowerResponse(
                borrower.getId(),
                borrower.getName(),
                borrower.getEmail(),
                borrower.getBorrowedBooks() == null ? null :
                        borrower.getBorrowedBooks().stream()
                                .map(BookMapper::toResponse)
                                .collect(Collectors.toList())
        );
    }

    public static Borrower toEntity(BorrowerRequest request) {
        if (request == null) return null;

        Borrower borrower = new Borrower();
        borrower.setName(request.getName());
        borrower.setEmail(request.getEmail());
        return borrower;
    }
}
