package com.example.sorting_app.sorting.impl;

import com.example.sorting_app.constant.SortAlgorithmKeys;
import com.example.sorting_app.sorting.AbstractSortAlgorithm;
import com.example.sorting_app.sorting.SortMetrics;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class InsertionSortAlgorithm extends AbstractSortAlgorithm {

    @Override
    public String getKey() {
        return SortAlgorithmKeys.INSERTION;
    }

    @Override
    public String getDisplayName() {
        return "Insertion Sort";
    }

    @Override
    public String getTimeComplexity() {
        return "Best: O(n) | Average/Worst: O(n^2)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(1)";
    }

    @Override
    public boolean isQuadratic() {
        return true;
    }

    @Override
    protected <T> void doSort(List<T> list, Comparator<T> comparator, SortMetrics metrics) {
        int n = list.size();
        for (int i = 1; i < n; i++) {
            T key = list.get(i);
            int j = i - 1;
            while (j >= 0) {
                metrics.incrementComparisons();
                if (comparator.compare(list.get(j), key) > 0) {
                    list.set(j + 1, list.get(j));
                    metrics.incrementWrites();
                    j--;
                } else {
                    break;
                }
            }
            list.set(j + 1, key);
            metrics.incrementWrites();
        }
    }
}