package com.huawei.sdn.commons.data;

import org.apache.commons.net.ntp.TimeStamp;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 8/18/14
 * Time: 6:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConnectorStatistics {

    private Date timeStamp = new Date();
    private String linkId;
    private double frameDelay;
    private double frameDelayVariation;
    private double packetLossRate;
    private long txPackets;
    private long txPacketsBase;
    private long rxPackets;
    private long rxPacketsBase;
    private long txBytes;
    private long txBytesBase;
    private long rxBytes;
    private long rxBytesBase;
    private long txDrops;
    private long txDropsBase;
    private long rxDrops;
    private long rxDropsBase;
    private long txErrors;
    private long txErrorsBase;
    private long rxErrors;
    private long rxErrorsBase;
    private int flows;

    public Date getDate() {
        return timeStamp;
    }

    public void setDate(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public double getFrameDelay() {
        return frameDelay;
    }

    public void setFrameDelay(double frameDelay) {
        this.frameDelay = frameDelay;
    }

    public double getFrameDelayVariation() {
        return frameDelayVariation;
    }

    public void setFrameDelayVariation(double frameDelayVariation) {
        this.frameDelayVariation = frameDelayVariation;
    }

    public double getPacketLossRate() {
        return packetLossRate;
    }

    public void setPacketLossRate(double packetLossRate) {
        this.packetLossRate = packetLossRate;
    }

    public long getTxPackets() {
        return txPackets - txPacketsBase;
    }

    public void setTxPackets(long txPackets) {
        this.txPackets = txPackets;
    }

    public long getRxPackets() {
        return rxPackets - rxPacketsBase;
    }

    public void setRxPackets(long rxPackets) {
        this.rxPackets = rxPackets;
    }

    public long getTxBytes() {
        return txBytes - txBytesBase;
    }

    public void setTxBytes(long txBytes) {
        this.txBytes = txBytes;
    }

    public long getRxBytes() {
        return rxBytes - rxBytesBase;
    }

    public void setRxBytes(long rxBytes) {
        this.rxBytes = rxBytes;
    }

    public long getTxDrops() {
        return txDrops - txDropsBase;
    }

    public void setTxDrops(long txDrops) {
        this.txDrops = txDrops;
    }

    public long getRxDrops() {
        return rxDrops - rxDropsBase;
    }

    public void setRxDrops(long rxDrops) {
        this.rxDrops = rxDrops;
    }

    public long getTxErrors() {
        return txErrors - txErrorsBase;
    }

    public void setTxErrors(long txErrors) {
        this.txErrors = txErrors;
    }

    public long getRxErrors() {
        return rxErrors - rxErrorsBase;
    }

    public void setRxErrors(long rxErrors) {
        this.rxErrors = rxErrors;
    }

    public int getFlows() {
        return flows;
    }

    public void setFlows(int flows) {
        this.flows = flows;
    }

    public void setBase(){
        txPacketsBase=  txPackets;
        rxPacketsBase =  rxPackets;
        txBytesBase = txBytes;
        rxBytesBase = rxBytes;
        txDropsBase = txDrops;
        rxDropsBase = rxDrops;
        txErrorsBase = txErrors;
        rxErrorsBase = rxErrors;
    }

    public void update(ConnectorStatistics updated){
        this.linkId = updated.getLinkId();
        this.frameDelay = updated.getFrameDelay();
        this.frameDelayVariation = updated.getFrameDelayVariation();
        this.packetLossRate = updated.getPacketLossRate();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConnectorStatistics that = (ConnectorStatistics) o;

        return Double.compare(that.frameDelay, frameDelay) == 0 &&
                Double.compare(that.frameDelayVariation, frameDelayVariation) == 0 &&
                Double.compare(that.packetLossRate, packetLossRate) == 0 &&
                !(linkId != null ? !linkId.equals(that.linkId) : that.linkId != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = linkId != null ? linkId.hashCode() : 0;
        temp = Double.doubleToLongBits(frameDelay);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(frameDelayVariation);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(packetLossRate);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "ConnectorStatistics{" +
                "linkId='" + linkId + '\'' +
                ", frameDelay=" + frameDelay +
                ", frameDelayVariation=" + frameDelayVariation +
                ", packetLossRate=" + packetLossRate +
                '}';
    }
}
