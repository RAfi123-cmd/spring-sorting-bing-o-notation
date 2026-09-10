package com.example.sorting_app.controller;

import com.example.sorting_app.constant.AppConstant;
import com.example.sorting_app.constant.SortAlgorithmKeys;
import com.example.sorting_app.constant.SortableFields;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(SortingConstant.BASE_PATH)
@RequiredArgsConstructor
public class SortingController {

    private final TransactionRepository transactionRepository;
    private final SortAlgorithmRegistry algorithmRegistry;

    @GetMapping(SortingConstant.ALGORITHMS_PATH)
    public List<Map<String, Object>> listAlgorithms() {
        return algorithmRegistry.getAll().stream()
                .map(a -> Map.<String, Object>of(
                        "key", a.getKey(),
                        "name", a.getDisplayName(),
                        "timeComplexity", a.getTimeComplexity(),
                        "spaceComplexity", a.getSpaceComplexity()))
                .toList();
    }


    @GetMapping(SortingConstant.SORT_PATH)
    public SortResponse sort(
            @RequestParam String algorithm,
            @RequestParam(defaultValue = SortableFields.UNIT_PRICE) String field,
            @RequestParam(defaultValue = AppConstant.ORDER_ASC) String order,
            @RequestParam(defaultValue = AppConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(defaultValue = AppConstant.DEFAULT_FORCE) boolean force) {

        SortAlgorithm algo = algorithmRegistry.get(algorithm);

        if (algo.isQuadratic() && limit > AppConstant.QUADRATIC_SAFE_LIMIT && !force) {
            throw new IllegalArgumentException(
                    algo.getDisplayName() + " is O(n^2); limit " + limit +
                            " exceeds the safe cap of " + AppConstant.QUADRATIC_SAFE_LIMIT +
                            ". Pass force=true to override (may be slow).");
        }

        List<Transaction> data = new ArrayList<>(
                transactionRepository.findAll(PageRequest.of(0, limit)).getContent());

        Comparator<Transaction> comparator = TransactionFieldComparators.forField(field, order);

        SortMetrics metrics = algo.sortAndMeasure(data, comparator);

        return new SortResponse(algo.getDisplayName(), algo.getTimeComplexity(), algo.getSpaceComplexity(),
                data.size(), metrics.getComparisons(), metrics.getWrites(),
                metrics.getPeakRecursionDepth(), metrics.getPeakAuxiliaryArrayElements(),
                metrics.getElapsedMillis(), data);
    }


    @GetMapping(SortingConstant.BENCHMARK_PATH)
    public List<BenchmarkResult> benchmark(
            @RequestParam(defaultValue = SortAlgorithmKeys.ALL_KEYS_CSV) String algorithms,
            @RequestParam(defaultValue = SortableFields.UNIT_PRICE) String field,
            @RequestParam(defaultValue = AppConstant.ORDER_ASC) String order,
            @RequestParam(defaultValue = AppConstant.DEFAULT_BENCMARK) String sizes,
            @RequestParam(defaultValue = AppConstant.DEFAULT_FORCE) boolean force) {

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
                            algo.getSpaceComplexity(), size, 0, 0, 0, 0, 0,
                            "skipped - only " + pool.size() + " records available in the database"));
                    continue;
                }
                if (algo.isQuadratic() && size > AppConstant.QUADRATIC_SAFE_LIMIT && !force) {
                    results.add(new BenchmarkResult(algo.getDisplayName(), algo.getTimeComplexity(),
                            algo.getSpaceComplexity(), size, 0, 0, 0, 0, 0,
                            "skipped - O(n^2)-class algorithm above safe cap of " +
                                    AppConstant.QUADRATIC_SAFE_LIMIT + " (use force=true to override)"));
                    continue;
                }

                List<Transaction> sample = new ArrayList<>(pool.subList(0, size));
                SortMetrics metrics = algo.sortAndMeasure(sample, comparator);

                results.add(new BenchmarkResult(algo.getDisplayName(), algo.getTimeComplexity(),
                        algo.getSpaceComplexity(), size, metrics.getComparisons(), metrics.getWrites(),
                        metrics.getPeakRecursionDepth(), metrics.getPeakAuxiliaryArrayElements(),
                        metrics.getElapsedMillis(), null));
            }
        }
        return results;
    }

    @ExceptionHandler({NoSuchElementException.class, IllegalArgumentException.class})
    public ResponseEntity<?> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
    }
}