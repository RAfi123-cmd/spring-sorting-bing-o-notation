package com.example.sorting_app.persitance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sorting_app.persitance.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByCountryIgnoreCase(String country, Pageable pageable);
}
