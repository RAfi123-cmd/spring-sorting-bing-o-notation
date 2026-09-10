package com.example.sorting_app.sorting;

import java.util.Comparator;
import java.util.List;

/**
 * Base class for every concrete sorting algorithm (OOP: Inheritance).
 *
 * This applies the Template Method pattern: {@link #sortAndMeasure} defines
 * the fixed skeleton — start a timer, delegate the actual sorting work to
 * the subclass, stop the timer — while each subclass only has to implement
 * {@link #doSort}, the part that actually varies between algorithms.
 *
 * Subclasses therefore never have to worry about timing or wiring up a
 * {@link SortMetrics} instance themselves; that responsibility is
 * encapsulated here (OOP: Encapsulation) and cannot be bypassed because
 * {@link #sortAndMeasure} is {@code final}.
 */
public abstract class AbstractSortAlgorithm implements SortAlgorithm {

    @Override
    public final <T> SortMetrics sortAndMeasure(List<T> list, Comparator<T> comparator) {
        SortMetrics metrics = new SortMetrics();
        long startTime = System.nanoTime();
        doSort(list, comparator, metrics);
        metrics.setElapsedNanos(System.nanoTime() - startTime);
        return metrics;
    }

    /**
     * The actual sorting algorithm, implemented in place on {@code list}.
     * Every comparison and every element move/swap must be reported through
     * {@code metrics} so the empirical Big-O analysis stays accurate.
     */
    protected abstract <T> void doSort(List<T> list, Comparator<T> comparator, SortMetrics metrics);
}