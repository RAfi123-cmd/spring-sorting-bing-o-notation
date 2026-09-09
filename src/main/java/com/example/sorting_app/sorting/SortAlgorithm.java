package com.example.sorting_app.sorting;

import java.util.Comparator;
import java.util.List;

public interface  SortAlgorithm {
    String getKey();

    String getDisplayName();

    String getTimeComplexity();

    boolean isQuadratic();

    <T> void sort(List<T> list, Comparator<T> Comparator, SortMetrics metrics);
}
