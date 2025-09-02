package com.example.library.external.repository;

import com.example.library.domain.boundary.repositories.BorrowerRepository;
import com.example.library.external.entities.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowerRepositoryInterface extends JpaRepository<Borrower, Long>, BorrowerRepository {
}
