package com.huawei.sdn.pathselector.web.pojo;

/**
 * Created by aWX209432 on 8/31/14.
 */
public class QdiscParams {
    private String linkId;
    private int delay;
    private int jitter;
    private int packetLossRate;

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getJitter() {
        return jitter;
    }

    public void setJitter(int jitter) {
        this.jitter = jitter;
    }

    public int getPacketLossRate() {
        return packetLossRate;
    }

    public void setPacketLossRate(int packetLossRate) {
        this.packetLossRate = packetLossRate;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    @Override
    public String toString() {
        return "QdiscParams{" +
                "linkId='" + linkId + '\'' +
                ", delay=" + delay +
                ", jitter=" + jitter +
                ", packetLossRate=" + packetLossRate +
                '}';
    }
}
