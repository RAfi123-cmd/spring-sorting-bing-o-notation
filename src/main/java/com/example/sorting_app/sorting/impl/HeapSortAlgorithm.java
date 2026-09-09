package com.example.sorting_app.sorting.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.sorting_app.sorting.SortAlgorithm;
import com.example.sorting_app.sorting.SortMetrics;


@Component 
public class HeapSortAlgorithm implements SortAlgorithm{

    @Override
    public String getKey() {
        // TODO Auto-generated method stub
        return "heap";
    }

    @Override
    public String getDisplayName() {
        // TODO Auto-generated method stub
        return "Heap Sort";
    }

    @Override
    public String getTimeComplexity() {
        // TODO Auto-generated method stub
        return "Best/Average/Worst: O(n log n)";
    }

    @Override
    public boolean isQuadratic() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <T> void sort(List<T> list, Comparator<T> comparator, SortMetrics metrics) {
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
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
    }
    
}
