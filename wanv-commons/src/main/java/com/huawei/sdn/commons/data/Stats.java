package com.huawei.sdn.commons.data;

/**
 * Created by root on 5/27/14.
 */
public class Stats {

    private long count;
    private double max;
    private double min;
    private double median;
    private double mean;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMedian() {
        return median;
    }

    public void setMedian(double median) {
        this.median = median;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    @Override
    public String toString() {
        return "Stats{" +
                "count=" + count +
                ", max=" + max +
                ", min=" + min +
                ", median=" + median +
                ", mean=" + mean +
                '}';
    }
}
