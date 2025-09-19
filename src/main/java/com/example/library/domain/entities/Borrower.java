package com.example.library.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "borrowers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        }
)
public class Borrower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false, unique = true) // ensures DB-level uniqueness
    private String email;

    @OneToMany(mappedBy = "borrower")
    private List<Book> borrowedBooks;
}


