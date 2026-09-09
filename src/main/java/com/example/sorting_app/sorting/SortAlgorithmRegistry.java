package com.example.sorting_app.sorting;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class SortAlgorithmRegistry {
    private final List<SortAlgorithm> algorithms;
    private  Map<String, SortAlgorithm> registry;

    private Map<String, SortAlgorithm> registry() {
        if (registry == null) {
            registry = algorithms.stream()
                    .collect(Collectors.toMap(a -> a.getKey().toLowerCase(), a -> a));
        }
        return registry;
    }

    public SortAlgorithm get(String key) {
        SortAlgorithm algorithm = registry().get(key.toLowerCase());
        if (algorithm == null) {
            throw new NoSuchElementException(
                    "Unknown algorithm '" + key + "'. Available: " + registry().keySet());
        }
        return algorithm;
    }

    public List<SortAlgorithm> getAll() {
        return algorithms;
    }
}
