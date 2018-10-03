package com.huawei.sdn.commons.data;

import org.apache.commons.net.ntp.TimeStamp;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class holds the current metrics for each link connected to this server.
 *
 * Created by root on 5/25/14.
 */
@JsonSerialize
public class CurrentLinkMetrics implements Comparable<CurrentLinkMetrics>{

    private String linkId;
    private int frameDelay;
    private int frameDelayVariation;
    private boolean isConnected;
    private double packetLossRate;
    public Map<String, Stats> currentStats = new HashMap<>();
    public String csvColumns;
    private Date timeStamp;

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setCurrentTime() {
        this.timeStamp = Calendar.getInstance().getTime();
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public int getFrameDelay() {
        return frameDelay;
    }

    public void setFrameDelay(int frameDelay) {
        this.frameDelay = frameDelay;
    }

    public int getFrameDelayVariation() {
        return frameDelayVariation;
    }

    public void setFrameDelayVariation(int frameDelayVariation) {
        this.frameDelayVariation = frameDelayVariation;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public double getPacketLossRate() {
        return packetLossRate;
    }

    public void setPacketLossRate(double packetLossRate) {
        this.packetLossRate = packetLossRate;
    }

    public Map<String, Stats> getCurrentStats() {
        return currentStats;
    }

    public void setCurrentStats(Map<String, Stats> currentStats) {
        this.currentStats = currentStats;
    }

    public String getCsvColumns() {
        return csvColumns;
    }

    public void setCsvColumns(String csvColumns) {
        this.csvColumns = csvColumns;
    }

    public void addStats(String key,Stats stats){
        currentStats.put(key,stats);
    }

    @Override
    public String toString() {
        return "CurrentLinkMetrics{" +
                "linkId='" + linkId + '\'' +
                ", frameDelay=" + frameDelay +
                ", frameDelayVariation=" + frameDelayVariation +
                ", isConnected=" + isConnected +
                ", packetLossRate=" + packetLossRate +
                ", currentStats=" + currentStats +
                ", csvColumns='" + csvColumns + '\'' +
                '}';
    }

    @Override
    public int compareTo(CurrentLinkMetrics other) {

        if(this.timeStamp.getTime() > other.timeStamp.getTime()){
            return 1;
        }

        if(this.timeStamp.getTime() < other.timeStamp.getTime()){
            return -1;

        }

        return 0;

    }
}
