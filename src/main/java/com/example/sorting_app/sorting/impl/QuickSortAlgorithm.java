package com.example.sorting_app.sorting.impl;

import com.example.sorting_app.constant.SortAlgorithmKeys;
import com.example.sorting_app.sorting.AbstractSortAlgorithm;
import com.example.sorting_app.sorting.SortMetrics;
import org.springframework.stereotype.Component;

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
        return "Average: O(log n) | Worst: O(n)";
    }

    @Override
    public boolean isQuadratic() {
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
                int[] equalRange = partition(list, low, high, comparator, metrics);
                quickSort(list, low, equalRange[0] - 1, comparator, metrics);
                quickSort(list, equalRange[1] + 1, high, comparator, metrics);
            } finally {
                metrics.exitRecursion();
            }
        }
    }

    private <T> int[] partition(List<T> list, int low, int high, Comparator<T> comparator, SortMetrics metrics) {
        int middle = low + (high - low) / 2;
        T pivot = median(list.get(low), list.get(middle), list.get(high), comparator, metrics);
        int less = low;
        int current = low;
        int greater = high;

        while (current <= greater) {
            metrics.incrementComparisons();
            int comparison = comparator.compare(list.get(current), pivot);
            if (comparison < 0) {
                swap(list, less++, current++, metrics);
            } else if (comparison > 0) {
                swap(list, current, greater--, metrics);
            } else {
                current++;
            }
        }
        return new int[]{less, greater};
    }

    private <T> T median(T first, T second, T third, Comparator<T> comparator, SortMetrics metrics) {
        metrics.incrementComparisons();
        if (comparator.compare(first, second) < 0) {
            metrics.incrementComparisons();
            return comparator.compare(second, third) < 0 ? second
                    : comparator.compare(first, third) < 0 ? third : first;
        }
        metrics.incrementComparisons();
        return comparator.compare(first, third) < 0 ? first
                : comparator.compare(second, third) < 0 ? third : second;
    }

    private <T> void swap(List<T> list, int first, int second, SortMetrics metrics) {
        if (first != second) {
            T value = list.get(first);
            list.set(first, list.get(second));
            list.set(second, value);
                metrics.incrementWrites();
        }
    }
}