package com.example.sorting_app.dto;

import java.util.List;
import java.util.Locale;

import com.example.sorting_app.persitance.entity.Transaction;

public class SortResponse {

    private String algorithm;
    private String timeComplexity;
    private String spaceComplexity;
    private int dataSize;
    private long comparisons;
    private long writes;
    private long peakRecursionDepth;
    private long peakAuxiliaryArrayElements;
    private String elapsedMillis;
    private List<Transaction> data;

    public SortResponse(
            String algorithm,
            String timeComplexity,
            String spaceComplexity,
            int dataSize,
            long comparisons,
            long writes,
            long peakRecursionDepth,
            long peakAuxiliaryArrayElements,
            double elapsedMillis,
            List<Transaction> data) {

        this.algorithm = algorithm;
        this.timeComplexity = timeComplexity;
        this.spaceComplexity = spaceComplexity;
        this.dataSize = dataSize;
        this.comparisons = comparisons;
        this.writes = writes;
        this.peakRecursionDepth = peakRecursionDepth;
        this.peakAuxiliaryArrayElements = peakAuxiliaryArrayElements;
        this.elapsedMillis = formatElapsedTime(elapsedMillis);
        this.data = data;
    }

    private String formatElapsedTime(double millis) {
        if (millis < 1) {
            return String.format(
                    Locale.US,
                    "%.4f µs",
                    millis * 1000
            );
        }

        if (millis < 1000) {
            return String.format(
                    Locale.US,
                    "%.4f ms",
                    millis
            );
        }

        return String.format(
                Locale.US,
                "%.4f s",
                millis / 1000
        );
    }

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

    public String getElapsedMillis() {
        return elapsedMillis;
    }

    public void setElapsedMillis(String elapsedMillis) {
        this.elapsedMillis = elapsedMillis;
    }

    public List<Transaction> getData() {
        return data;
    }

    public void setData(List<Transaction> data) {
        this.data = data;
    }
}