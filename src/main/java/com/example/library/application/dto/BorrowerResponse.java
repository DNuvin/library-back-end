package com.example.library.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowerResponse {
    private Long id;
    private String name;
    private String email;
    private List<BookResponse> borrowedBooks; // optional
}
