package com.example.sorting_app.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor 
public class BenchmarkResult {
    private String algorithm;
    private String timeComplexity;
    private String spaceComplexity;
    private int dataSize;
    private long comparisons;
    private long writes;
    private long peakRecursionDepth;
    private long peakAuxiliaryArrayElements;
    private double elapsedMillis;
    private String note;
    
    public String getAlgorithm() {
        return algorithm;
    }
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
    public String getTimeComplexity() {
        return timeComplexity;
    }
    public void setTimeComplexity(String timeComplexity) {
        this.timeComplexity = timeComplexity;
    }

    public String getSpaceComplexity() {
        return spaceComplexity;
    }
    public void setSpaceComplexity(String spaceComplexity) {
        this.spaceComplexity = spaceComplexity;
    }

    public int getDataSize() {
        return dataSize;
    }
    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }
    public long getComparisons() {
        return comparisons;
    }
    public void setComparisons(long comparisons) {
        this.comparisons = comparisons;
    }
    public long getWrites() {
        return writes;
    }
    public void setWrites(long writes) {
        this.writes = writes;
    }
    public double getElapsedMillis() {
        return elapsedMillis;
    }
    public void setElapsedMillis(double elapsedMillis) {
        this.elapsedMillis = elapsedMillis;
    }
    public long getPeakRecursionDepth() {
        return peakRecursionDepth;
    }
    public void setPeakRecursionDepth(long peakRecursionDepth) {
        this.peakRecursionDepth = peakRecursionDepth;
    }
    public long getPeakAuxiliaryArrayElements() {
        return peakAuxiliaryArrayElements;
    }
    public void setPeakAuxiliaryArrayElements(long peakAuxiliaryArrayElements) {
        this.peakAuxiliaryArrayElements = peakAuxiliaryArrayElements;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
}
