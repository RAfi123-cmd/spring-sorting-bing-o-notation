package com.example.sorting_app.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.sorting_app.constant.SortingConstant;
import com.example.sorting_app.dto.BenchmarkResult;
import com.example.sorting_app.dto.SortResponse;
import com.example.sorting_app.persitance.entity.Transaction;
import com.example.sorting_app.persitance.repository.TransactionRepository;
import com.example.sorting_app.sorting.SortAlgorithm;
import com.example.sorting_app.sorting.SortAlgorithmRegistry;
import com.example.sorting_app.sorting.SortMetrics;
import com.example.sorting_app.sorting.TransactionFieldComparators;

import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping (SortingConstant.BASE_PATH)
@RequiredArgsConstructor
public class SortingController {
    private static final int QUADRATIC_SAFE_LIMIT = 20_000;

    private final TransactionRepository transactionRepository;
    private final SortAlgorithmRegistry algorithmRegistry;

    @GetMapping(SortingConstant.ALGORITHMS_PATH)
    public ResponseEntity<?> listAlgorithms() {

        List<Map<String, Object>> algorithms = algorithmRegistry.getAll().stream()
                .map(a -> Map.<String, Object>of(
                        "key", a.getKey(),
                        "name", a.getDisplayName(),
                        "complexity", a.getTimeComplexity()))
                .toList();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Algorithms retrieved successfully");
        response.put("data", algorithms);

        return ResponseEntity.ok(response);
    }

   
    @GetMapping(SortingConstant.SORT_PATH)
    public SortResponse sort(
            @RequestParam String algorithm,
            @RequestParam(defaultValue = "unitPrice") String field,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(defaultValue = "1000") int limit,
            @RequestParam(defaultValue = "false") boolean force) {

        SortAlgorithm algo = algorithmRegistry.get(algorithm);

        if (algo.isQuadratic() && limit > QUADRATIC_SAFE_LIMIT && !force) {
            throw new IllegalArgumentException(
                    algo.getDisplayName() + " is O(n^2); limit " + limit +
                            " exceeds the safe cap of " + QUADRATIC_SAFE_LIMIT +
                            ". Pass force=true to override (may be slow).");
        }

        List<Transaction> data = new ArrayList<>(
                transactionRepository.findAll(PageRequest.of(0, limit)).getContent());

        Comparator<Transaction> comparator = TransactionFieldComparators.forField(field, order);
        SortMetrics metrics = new SortMetrics();

        long start = System.nanoTime();
        algo.sort(data, comparator, metrics);
        metrics.setElapsedNanos(System.nanoTime() - start);

        return new SortResponse(algo.getDisplayName(), algo.getTimeComplexity(), data.size(),
                metrics.getComparisons(), metrics.getWrites(), metrics.getElapsedMillis(), data);
    }

    @GetMapping(SortingConstant.BENCHMARK_PATH)
    public List<BenchmarkResult> benchmark(
            @RequestParam(defaultValue = "bubble,selection,insertion,merge,quick,heap") String algorithms,
            @RequestParam(defaultValue = "unitPrice") String field,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(defaultValue = "100,1000,5000,10000") String sizes,
            @RequestParam(defaultValue = "false") boolean force) {

        List<String> algoKeys = Arrays.stream(algorithms.split(",")).map(String::trim).toList();
        List<Integer> dataSizes = Arrays.stream(sizes.split(","))
                .map(String::trim).map(Integer::parseInt).toList();

        Comparator<Transaction> comparator = TransactionFieldComparators.forField(field, order);

        int maxSize = dataSizes.stream().max(Integer::compareTo).orElse(0);
        List<Transaction> pool = new ArrayList<>(
                transactionRepository.findAll(PageRequest.of(0, maxSize)).getContent());

        List<BenchmarkResult> results = new ArrayList<>();

        for (String key : algoKeys) {
            SortAlgorithm algo = algorithmRegistry.get(key);
            for (int size : dataSizes) {
                if (size > pool.size()) {
                    results.add(new BenchmarkResult(algo.getDisplayName(), algo.getTimeComplexity(),
                            size, 0, 0, 0,
                            "skipped - only " + pool.size() + " records available in the database"));
                    continue;
                }
                if (algo.isQuadratic() && size > QUADRATIC_SAFE_LIMIT && !force) {
                    results.add(new BenchmarkResult(algo.getDisplayName(), algo.getTimeComplexity(),
                            size, 0, 0, 0,
                            "skipped - O(n^2)-class algorithm above safe cap of " +
                                    QUADRATIC_SAFE_LIMIT + " (use force=true to override)"));
                    continue;
                }

                List<Transaction> sample = new ArrayList<>(pool.subList(0, size));
                SortMetrics metrics = new SortMetrics();
                long start = System.nanoTime();
                algo.sort(sample, comparator, metrics);
                metrics.setElapsedNanos(System.nanoTime() - start);

                results.add(new BenchmarkResult(algo.getDisplayName(), algo.getTimeComplexity(),
                        size, metrics.getComparisons(), metrics.getWrites(), metrics.getElapsedMillis(), null));
            }
        }
        return results;
    }

    @ExceptionHandler({NoSuchElementException.class, IllegalArgumentException.class})
    public ResponseEntity<?> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
    }
}
