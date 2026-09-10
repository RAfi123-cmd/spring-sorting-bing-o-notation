package com.example.sorting_app.sorting.impl;

import com.example.sorting_app.constant.SortAlgorithmKeys;
import com.example.sorting_app.sorting.AbstractSortAlgorithm;
import com.example.sorting_app.sorting.SortMetrics;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class QuickSortAlgorithm extends AbstractSortAlgorithm {

    @Override
    public String getKey() {
        return SortAlgorithmKeys.QUICK;
    }

    @Override
    public String getDisplayName() {
        return "Quick Sort";
    }

    @Override
    public String getTimeComplexity() {
        return "Best/Average: O(n log n) | Worst: O(n^2)";
    }

    @Override
    public String getSpaceComplexity() {
        // In-place partitioning, but recursion uses call-stack space:
        // O(log n) average, degrades to O(n) worst case (unbalanced partitions).
        return "Average: O(log n) | Worst: O(n)";
    }

    @Override
    public boolean isQuadratic() {
        // Worst case is quadratic (already-sorted input with last-element pivot),
        // so it is grouped with the guarded algorithms for large benchmark sizes.
        return true;
    }

    @Override
    protected <T> void doSort(List<T> list, Comparator<T> comparator, SortMetrics metrics) {
        if (list.size() < 2) return;
        quickSort(list, 0, list.size() - 1, comparator, metrics);
    }

    private <T> void quickSort(List<T> list, int low, int high, Comparator<T> comparator, SortMetrics metrics) {
        if (low < high) {
            metrics.enterRecursion();
            try {
                int pivotIndex = partition(list, low, high, comparator, metrics);
                quickSort(list, low, pivotIndex - 1, comparator, metrics);
                quickSort(list, pivotIndex + 1, high, comparator, metrics);
            } finally {
                metrics.exitRecursion();
            }
        }
    }

    private <T> int partition(List<T> list, int low, int high, Comparator<T> comparator, SortMetrics metrics) {
        T pivot = list.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            metrics.incrementComparisons();
            if (comparator.compare(list.get(j), pivot) <= 0) {
                i++;
                Collections.swap(list, i, j);
                metrics.incrementWrites();
            }
        }
        Collections.swap(list, i + 1, high);
        metrics.incrementWrites();
        return i + 1;
    }
}