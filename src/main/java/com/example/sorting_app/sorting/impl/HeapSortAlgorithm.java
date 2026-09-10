package com.example.sorting_app.sorting.impl;

import com.example.sorting_app.constant.SortAlgorithmKeys;
import com.example.sorting_app.sorting.AbstractSortAlgorithm;
import com.example.sorting_app.sorting.SortMetrics;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class HeapSortAlgorithm extends AbstractSortAlgorithm {

    @Override
    public String getKey() {
        return SortAlgorithmKeys.HEAP;
    }

    @Override
    public String getDisplayName() {
        return "Heap Sort";
    }

    @Override
    public String getTimeComplexity() {
        return "Best/Average/Worst: O(n log n)";
    }

    @Override
    public String getSpaceComplexity() {
        // Textbook answer is O(1) because heapify can be written iteratively.
        // Our implementation uses recursive heapify for readability, which
        // technically adds O(log n) call-stack space - visible in the
        // measured peakRecursionDepth even though no auxiliary array is used.
        return "O(1) (iterative) / O(log n) stack for this recursive implementation";
    }

    @Override
    public boolean isQuadratic() {
        return false;
    }

    @Override
    protected <T> void doSort(List<T> list, Comparator<T> comparator, SortMetrics metrics) {
        int n = list.size();
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(list, n, i, comparator, metrics);
        }
        for (int i = n - 1; i > 0; i--) {
            Collections.swap(list, 0, i);
            metrics.incrementWrites();
            heapify(list, i, 0, comparator, metrics);
        }
    }

    private <T> void heapify(List<T> list, int size, int rootIndex, Comparator<T> comparator, SortMetrics metrics) {
        metrics.enterRecursion();
        try {
            int largest = rootIndex;
            int left = 2 * rootIndex + 1;
            int right = 2 * rootIndex + 2;

            if (left < size) {
                metrics.incrementComparisons();
                if (comparator.compare(list.get(left), list.get(largest)) > 0) {
                    largest = left;
                }
            }
            if (right < size) {
                metrics.incrementComparisons();
                if (comparator.compare(list.get(right), list.get(largest)) > 0) {
                    largest = right;
                }
            }
            if (largest != rootIndex) {
                Collections.swap(list, rootIndex, largest);
                metrics.incrementWrites();
                heapify(list, size, largest, comparator, metrics);
            }
        } finally {
            metrics.exitRecursion();
        }
    }
}