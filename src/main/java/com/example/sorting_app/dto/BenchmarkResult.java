package com.example.sorting_app.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor 
public class BenchmarkResult {
    private String algorithm;
    private String timeComplexity;
    private int dateSize;
    private long comparisons;
    private long writes;
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
    public int getDateSize() {
        return dateSize;
    }
    public void setDateSize(int dateSize) {
        this.dateSize = dateSize;
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
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
}
