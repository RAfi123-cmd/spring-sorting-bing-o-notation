package com.example.sorting_app.sorting;

import java.util.Comparator;
import java.util.List;

public interface  SortAlgorithm {
    String getKey();

    String getDisplayName();

    String getTimeComplexity();

    String getSpaceComplexity();

    boolean isQuadratic();

    <T> SortMetrics sortAndMeasure(List<T> list, Comparator<T> comparator);
}
