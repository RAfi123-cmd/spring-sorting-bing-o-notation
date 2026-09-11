package com.example.sorting_app.sorting;

import lombok.Getter;

@Getter 
public class SortMetrics {
    private long comparisons = 0;
    private long writes = 0;
    private long elapsedNanos = 0;

    private long currentRecursionDepth = 0;
    private long peakRecursionDepth = 0;
 
    private long currentAuxiliaryArrayElements = 0;
    private long peakAuxiliaryArrayElements = 0;


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

    public void exitRecursion(){
        currentRecursionDepth--;
    }

    public void enterRecursion(){
        currentRecursionDepth++;
        if (currentRecursionDepth > peakRecursionDepth) {
            peakRecursionDepth = currentRecursionDepth;
        }
    }

    public void allocateAuxiliaryArray(long elements){
        currentAuxiliaryArrayElements += elements;
        if (currentAuxiliaryArrayElements > peakAuxiliaryArrayElements) {
            peakAuxiliaryArrayElements = currentAuxiliaryArrayElements;
        }
    }

    public void releaseAuxiliaryArray(long elements){
        currentAuxiliaryArrayElements -= elements;
    }

    public long getTotalAuxiliaryArray(){
        return peakRecursionDepth + peakAuxiliaryArrayElements;
    }

    
}
