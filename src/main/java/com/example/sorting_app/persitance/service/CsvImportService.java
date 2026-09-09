package com.example.sorting_app.persitance.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.sorting_app.persitance.entity.Transaction;
import com.example.sorting_app.persitance.repository.TransactionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Service 
@RequiredArgsConstructor 
public class CsvImportService {
    private final TransactionRepository transactionRepository;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("M/d/yyyy H:mm");

    private static final int BATCH_SIZE = 500;

    @Transactional
    public ImportResult importCsv(MultipartFile file) throws IOException {
        int successCount = 0;
        int errorCount = 0;
        List<Transaction> batch = new ArrayList<>(BATCH_SIZE);

        // The dataset is encoded as ISO-8859-1 / Windows-1252 (it contains the £ symbol).
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.ISO_8859_1));
             CSVParser parser = CSVFormat
             .DEFAULT.builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setIgnoreSurroundingSpaces(true)
                     .setTrim(true)
                     .setIgnoreEmptyLines(true)
                     .build()
                     .parse(reader)) {

            for (CSVRecord record : parser) {
                try {
                    batch.add(mapRecord(record));
                    successCount++;

                    if (batch.size() >= BATCH_SIZE) {
                        transactionRepository.saveAll(batch);
                        batch.clear();
                    }
                } catch (Exception ex) {
                    errorCount++;
                    log.warn("Skipping row {}: {}", record.getRecordNumber(), ex.getMessage());
                }
            }
            if (!batch.isEmpty()) {
                transactionRepository.saveAll(batch);
            }
        }
        return new ImportResult(successCount, errorCount);
    }

    private Transaction mapRecord(CSVRecord record){
        return Transaction.builder()
            .invoiceNo(get(record, "InvoiceNo"))
                .stockCode(get(record, "StockCode"))
                .description(get(record, "Description"))
                .quantity(parseInt(get(record, "Quantity")))
                .invoiceDate(parseDate(get(record, "InvoiceDate")))
                .unitPrice(parseDouble(get(record, "UnitPrice")))
                .customerId(parseLong(get(record, "CustomerID")))
                .country(get(record, "Country"))
                .build();
    }

    private String get(CSVRecord record, String column){
        return record.isMapped(column) ? record.get(column) : null;
    }

    private Integer parseInt(String v){
        try {
            return (v == null || v.isBlank()) ? null : Integer.parseInt(v.trim());
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    private Long parseLong(String v){
        try {
            if (v == null || v.isBlank()) return null;
            return (long) Double.parseDouble(v.trim());
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    private Double parseDouble (String v){
        try {
            return (v == null || v.isBlank()) ? null : Double.parseDouble(v.trim());
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    private LocalDateTime parseDate(String v){
        try {
            return (v == null || v.isBlank()) ? null : LocalDateTime.parse(v.trim(), DATE_FORMAT);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }


    public record ImportResult(int successCount, int errorCount){}

}
