package com.example.sorting_app.sorting;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractSortAlgorithm implements SortAlgorithm {

    @Override
    public final <T> SortMetrics sortAndMeasure(List<T> list, Comparator<T> comparator) {
        SortMetrics metrics = new SortMetrics();
        long startTime = System.nanoTime();
        doSort(list, comparator, metrics);
        metrics.setElapsedNanos(System.nanoTime() - startTime);
        return metrics;
    }
    protected abstract <T> void doSort(List<T> list, Comparator<T> comparator, SortMetrics metrics);
}