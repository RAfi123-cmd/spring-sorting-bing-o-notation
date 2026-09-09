package com.example.sorting_app.sorting.impl;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.sorting_app.sorting.SortAlgorithm;
import com.example.sorting_app.sorting.SortMetrics;

@Component 
public class InsertionSortAlgorithm implements SortAlgorithm{

    @Override
    public String getKey() {
        // TODO Auto-generated method stub
        return "insertion";
    }

    @Override
    public String getDisplayName() {
        // TODO Auto-generated method stub
        return "Insertion Sort";
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
        for (int i = 1; i < n; i++) {
            T key = list.get(i);
            int j = i - 1;
            while (j >= 0) {
                metrics.incrementComparisons();
                if (comparator.compare(list.get(j), key) > 0) {
                    list.set(j + 1, list.get(j));
                    metrics.incrementWrites();
                    j--;
                }else{
                    break;
                }
            }
            list.set(j + 1, key);
            metrics.incrementWrites();
        }
    }
    
}
