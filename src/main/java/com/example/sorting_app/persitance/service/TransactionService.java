package com.example.sorting_app.persitance.service;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.sorting_app.persitance.entity.Transaction;
import com.example.sorting_app.persitance.repository.TransactionRepository;

@Service 
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public Page<Transaction> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Transaction findById(Long id){
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Transaction Not Found: " + id));
    }

    public Transaction create(Transaction transaction){
        transaction.setId(null);
        return repository.save(transaction);
    }

    public Transaction update(Long id, Transaction updated){
        Transaction existing = findById(id);
        updated.setId(existing.getId());
        return repository.save(updated);
    }

    public void delete(Long id){
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Transaction Not Found: " + id);
        }
        repository.deleteById(id);
    }
}
