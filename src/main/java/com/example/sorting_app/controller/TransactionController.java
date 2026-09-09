package com.example.sorting_app.controller;

import com.example.sorting_app.constant.TransactionConstant;
import com.example.sorting_app.persitance.entity.Transaction;
import com.example.sorting_app.persitance.service.CsvImportService;
import com.example.sorting_app.persitance.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(TransactionConstant.BASE_PATH)
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final CsvImportService csvImportService;

    @GetMapping(TransactionConstant.ALL_PATH)
    public Page<Transaction> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Sort.Direction dir = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));
        return transactionService.findAll(pageable);
    }

    @GetMapping(TransactionConstant.DETAIL_PATH)
    public Transaction getById(@PathVariable Long id) {
        return transactionService.findById(id);
    }

    @PostMapping(TransactionConstant.CREATE_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction create(@RequestBody Transaction transaction) {
        return transactionService.create(transaction);
    }

    @PutMapping(TransactionConstant.EDIT_PATH)
    public Transaction update(@PathVariable Long id, @RequestBody Transaction transaction) {
        return transactionService.update(id, transaction);
    }

    @DeleteMapping(TransactionConstant.DELETE_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        transactionService.delete(id);
    }

    @GetMapping(TransactionConstant.COUNT_PATH)
    public Map<String, Long> count() {
        return Map.of("totalRecords", transactionService.findAll(Pageable.unpaged()).getTotalElements());
    }

    @PostMapping(value = TransactionConstant.IMPORT_PATH, consumes = "multipart/form-data")
    public ResponseEntity<?> importCsv(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "File is empty"));
        }
        CsvImportService.ImportResult result = csvImportService.importCsv(file);
        return ResponseEntity.ok(Map.of(
                "imported", result.successCount(),
                "skipped", result.errorCount()
        ));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNotFound(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }
}
