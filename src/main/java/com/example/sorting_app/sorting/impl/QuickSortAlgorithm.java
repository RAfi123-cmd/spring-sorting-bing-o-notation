package com.example.sorting_app.sorting.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.sorting_app.sorting.SortAlgorithm;
import com.example.sorting_app.sorting.SortMetrics;

@Component
public class QuickSortAlgorithm implements SortAlgorithm {

    @Override
    public String getKey() {
        // TODO Auto-generated method stub
        return "quick";
    }

    @Override
    public String getDisplayName() {
        // TODO Auto-generated method stub
        return "Quick Sort";
    }

    @Override
    public String getTimeComplexity() {
        // TODO Auto-generated method stub
        return "Best/Average: O(n log n) | Worst: O(n^2)";
    }

    @Override
    public boolean isQuadratic() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public <T> void sort(List<T> list, Comparator<T> comparator, SortMetrics metrics) {
        // TODO Auto-generated method stub
        if (list.size() < 2) return;
        quickSort(list, 0, list.size() - 1, comparator, metrics);
    }

    private <T> void quickSort(List<T> list, int low, int high, Comparator<T> comparator, SortMetrics metrics) {
        if (low < high) {
            int pivotIndex = partition(list, low, high, comparator, metrics);
            quickSort(list, low, pivotIndex - 1, comparator, metrics);
            quickSort(list, pivotIndex + 1, high, comparator, metrics);
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
