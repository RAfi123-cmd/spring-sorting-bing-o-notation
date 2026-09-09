package com.example.sorting_app.sorting.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.sorting_app.sorting.SortAlgorithm;
import com.example.sorting_app.sorting.SortMetrics;

@Component 
public class MergeSortAlgorithm implements  SortAlgorithm{

    @Override
    public String getKey() {
        // TODO Auto-generated method stub
        return "merge";
    }
    @Override
    public String getDisplayName() {
        // TODO Auto-generated method stub
        return "Merge Sort";
    }

    @Override
    public String getTimeComplexity() {
        // TODO Auto-generated method stub
        return "Merge/Average/Worst: O(n log n)";
    }

    @Override
    public boolean isQuadratic() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <T> void sort(List<T> list, Comparator<T> comparator, SortMetrics metrics) {
        // TODO Auto-generated method stub
        if(list.size() < 2) return ;
        mergeSort(list, 0, list.size() - 1, comparator, metrics);
    }

    private <T> void mergeSort(List<T> list, int left, int right, Comparator<T> comparator, SortMetrics metrics){
        if (left >= right) return;
            int mid = left + (right - left) / 2;
        mergeSort(list, left, mid, comparator, metrics);
        mergeSort(list, mid + 1, right, comparator, metrics);
        merge(list, left, mid, right, comparator, metrics);
    }

    private <T> void merge(List<T> list, int left, int mid, int right, Comparator <T> comparator, SortMetrics metrics){
        List<T> leftPart = new ArrayList<>(list.subList(left, mid + 1));
        List<T> rightPart = new ArrayList<>(list.subList(mid + 1, right + 1));

        int i = 0, j = 0, k = left;
        while (i < leftPart.size() && j < rightPart.size()) {
            metrics.incrementComparisons();
            if (comparator.compare(leftPart.get(i), rightPart.get(j)) <= 0) {
                list.set(k++, leftPart.get(i++));
            } else {
                list.set(k++, rightPart.get(j++));
            }
            metrics.incrementWrites();
        }
        while (i < leftPart.size()) {
            list.set(k++, leftPart.get(i++));
            metrics.incrementWrites();
        }
        while (j < rightPart.size()) {
            list.set(k++, rightPart.get(j++));
            metrics.incrementWrites();
        }
    }
    
}
