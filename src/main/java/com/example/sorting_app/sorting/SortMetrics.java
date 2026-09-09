package com.example.sorting_app.sorting;

import lombok.Getter;

@Getter 
public class SortMetrics {
    private long comparisons = 0;
    private long writes = 0;
    private long elapsedNanos = 0;


    public void incrementComparisons(){
        comparisons++;
    }

    public void incrementWrites(){
        writes++;
    }

    public void setElapsedNanos(long elapsedNanos) {
        this.elapsedNanos = elapsedNanos;
    }

    public double getElapsedMillis(){
        return elapsedNanos / 1_000_000.0;
    }

    
}
