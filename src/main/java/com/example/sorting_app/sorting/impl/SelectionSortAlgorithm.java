package com.example.sorting_app.sorting.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.sorting_app.sorting.AbstractSortAlgorithm;
import com.example.sorting_app.sorting.SortMetrics;

@Component
public class SelectionSortAlgorithm extends AbstractSortAlgorithm{

    @Override
    public String getKey() {
        // TODO Auto-generated method stub
        return "selection";
    }

    @Override
    public String getDisplayName() {
        // TODO Auto-generated method stub
        return "Selection Merge";
    }

    @Override
    public String getTimeComplexity() {
        // TODO Auto-generated method stub
        return "Best/Average/Worst: O(n^2)";
    }

    @Override
    public boolean isQuadratic() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public String getSpaceComplexity() {
        // TODO Auto-generated method stub
        return "O(1)";
    }

    @Override
    protected <T> void doSort(List<T> list, Comparator<T> comparator, SortMetrics metrics) {
        // TODO Auto-generated method stub
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                metrics.incrementComparisons();
                if (comparator.compare(list.get(j), list.get(minIdx)) < 0) {
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                Collections.swap(list, i, minIdx);
                metrics.incrementWrites();
            }
        }
    }

    
}
