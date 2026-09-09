package com.example.sorting_app.sorting.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.sorting_app.sorting.SortAlgorithm;
import com.example.sorting_app.sorting.SortMetrics;

@Component 
public class BubbleSortAlgorithm implements SortAlgorithm{

    @Override
    public String getKey() {
        // TODO Auto-generated method stub
        return "bubble";
    }

    @Override
    public String getDisplayName() {
        // TODO Auto-generated method stub
        return "Bubble Sort";
    }

    @Override
    public String getTimeComplexity() {
        // TODO Auto-generated method stub
        return "Best: O(n) | Average/Worst: O(n^2)";
    }

    @Override
    public boolean isQuadratic() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public <T> void sort(List<T> list, Comparator<T> comparator, SortMetrics metrics) {
        // TODO Auto-generated method stub
        int n = list.size();
        for (int i = 0; i < n; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1; j++) {
                metrics.incrementComparisons();
                if (comparator.compare(list.get(j), list.get(j + 1)) > 0) {
                    Collections.swap(list, j, j + 1);
                    metrics.incrementWrites();
                    swapped = true;
                }
            }
            if(!swapped) break;
        }
    }
}
