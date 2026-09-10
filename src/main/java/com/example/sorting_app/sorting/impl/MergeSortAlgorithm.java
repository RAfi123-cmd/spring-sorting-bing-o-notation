package com.example.sorting_app.sorting.impl;

import com.example.sorting_app.constant.SortAlgorithmKeys;
import com.example.sorting_app.sorting.AbstractSortAlgorithm;
import com.example.sorting_app.sorting.SortMetrics;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class MergeSortAlgorithm extends AbstractSortAlgorithm {

    @Override
    public String getKey() {
        return SortAlgorithmKeys.MERGE;
    }

    @Override
    public String getDisplayName() {
        return "Merge Sort";
    }

    @Override
    public String getTimeComplexity() {
        return "Best/Average/Worst: O(n log n)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(n)";
    }

    @Override
    public boolean isQuadratic() {
        return false;
    }

    @Override
    protected <T> void doSort(List<T> list, Comparator<T> comparator, SortMetrics metrics) {
        if (list.size() < 2) return;
        mergeSort(list, 0, list.size() - 1, comparator, metrics);
    }

    private <T> void mergeSort(List<T> list, int left, int right, Comparator<T> comparator, SortMetrics metrics) {
        if (left >= right) return;
        metrics.enterRecursion();
        try {
            int mid = left + (right - left) / 2;
            mergeSort(list, left, mid, comparator, metrics);
            mergeSort(list, mid + 1, right, comparator, metrics);
            merge(list, left, mid, right, comparator, metrics);
        } finally {
            metrics.exitRecursion();
        }
    }

    private <T> void merge(List<T> list, int left, int mid, int right, Comparator<T> comparator, SortMetrics metrics) {
        int leftSize = mid - left + 1;
        int rightSize = right - mid;

        metrics.allocateAuxiliaryArray(leftSize + rightSize);
        try {
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
        } finally {
            metrics.releaseAuxiliaryArray(leftSize + rightSize);
        }
    }
}