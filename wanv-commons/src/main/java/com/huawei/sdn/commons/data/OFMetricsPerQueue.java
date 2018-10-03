package com.huawei.sdn.commons.data;

public class OFMetricsPerQueue {

    private String portId;

    private String queueId;

    private long bytes;

    private long pkts;

    private long errors;

    private long duration;

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
    }

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    public long getBytes() {
        return bytes;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public long getPkts() {
        return pkts;
    }

    public void setPkts(long pkts) {
        this.pkts = pkts;
    }

    public long getErrors() {
        return errors;
    }

    public void setErrors(long errors) {
        this.errors = errors;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

}
