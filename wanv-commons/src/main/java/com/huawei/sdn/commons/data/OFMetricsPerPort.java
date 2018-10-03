package com.huawei.sdn.commons.data;

import java.util.List;

public class OFMetricsPerPort {

    private String portId;

    private long receivePackets;

    private long transmitPackets;

    private long receiveBytes;

    private long transmitBytes;

    private long receiveDrops;

    private long transmitDrops;

    private long receiveErrors;

    private long transmitErrors;

    private long receiveFrameError;

    private long receiveOverRunError;

    private long receiveCrcError;

    private long collisionCount;

    private List<OFMetricsPerQueue> metricsPerQueues;

    public OFMetricsPerPort() {
    }

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
    }

    public long getReceivePackets() {
        return receivePackets;
    }

    public void setReceivePackets(long receivePackets) {
        this.receivePackets = receivePackets;
    }

    public long getTransmitPackets() {
        return transmitPackets;
    }

    public void setTransmitPackets(long transmitPackets) {
        this.transmitPackets = transmitPackets;
    }

    public long getReceiveBytes() {
        return receiveBytes;
    }

    public void setReceiveBytes(long receiveBytes) {
        this.receiveBytes = receiveBytes;
    }

    public long getTransmitBytes() {
        return transmitBytes;
    }

    public void setTransmitBytes(long transmitBytes) {
        this.transmitBytes = transmitBytes;
    }

    public long getReceiveDrops() {
        return receiveDrops;
    }

    public void setReceiveDrops(long receiveDrops) {
        this.receiveDrops = receiveDrops;
    }

    public long getTransmitDrops() {
        return transmitDrops;
    }

    public void setTransmitDrops(long transmitDrops) {
        this.transmitDrops = transmitDrops;
    }

    public long getReceiveErrors() {
        return receiveErrors;
    }

    public void setReceiveErrors(long receiveErrors) {
        this.receiveErrors = receiveErrors;
    }

    public long getTransmitErrors() {
        return transmitErrors;
    }

    public void setTransmitErrors(long transmitErrors) {
        this.transmitErrors = transmitErrors;
    }

    public long getReceiveFrameError() {
        return receiveFrameError;
    }

    public void setReceiveFrameError(long receiveFrameError) {
        this.receiveFrameError = receiveFrameError;
    }

    public long getReceiveOverRunError() {
        return receiveOverRunError;
    }

    public void setReceiveOverRunError(long receiveOverRunError) {
        this.receiveOverRunError = receiveOverRunError;
    }

    public long getReceiveCrcError() {
        return receiveCrcError;
    }

    public void setReceiveCrcError(long receiveCrcError) {
        this.receiveCrcError = receiveCrcError;
    }

    public long getCollisionCount() {
        return collisionCount;
    }

    public void setCollisionCount(long collisionCount) {
        this.collisionCount = collisionCount;
    }

    public List<OFMetricsPerQueue> getMetricsPerQueues() {
        return metricsPerQueues;
    }

    public void setMetricsPerQueues(List<OFMetricsPerQueue> metricsPerQueues) {
        this.metricsPerQueues = metricsPerQueues;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (collisionCount ^ collisionCount >>> 32);
        result = prime * result + (portId == null ? 0 : portId.hashCode());
        result = prime * result + (int) (receiveBytes ^ receiveBytes >>> 32);
        result = prime * result + (int) (receiveCrcError ^ receiveCrcError >>> 32);
        result = prime * result + (int) (receiveDrops ^ receiveDrops >>> 32);
        result = prime * result + (int) (receiveErrors ^ receiveErrors >>> 32);
        result = prime * result + (int) (receiveFrameError ^ receiveFrameError >>> 32);
        result = prime * result + (int) (receiveOverRunError ^ receiveOverRunError >>> 32);
        result = prime * result + (int) (receivePackets ^ receivePackets >>> 32);
        result = prime * result + (int) (transmitBytes ^ transmitBytes >>> 32);
        result = prime * result + (int) (transmitDrops ^ transmitDrops >>> 32);
        result = prime * result + (int) (transmitErrors ^ transmitErrors >>> 32);
        result = prime * result + (int) (transmitPackets ^ transmitPackets >>> 32);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OFMetricsPerPort other = (OFMetricsPerPort) obj;
        if (collisionCount != other.collisionCount) {
            return false;
        }
        if (portId == null) {
            if (other.portId != null) {
                return false;
            }
        } else if (!portId.equals(other.portId)) {
            return false;
        }

        return receiveBytes == other.receiveBytes &&
                receiveCrcError == other.receiveCrcError &&
                receiveDrops == other.receiveDrops &&
                receiveErrors == other.receiveErrors &&
                receiveFrameError == other.receiveFrameError &&
                receiveOverRunError == other.receiveOverRunError &&
                receivePackets == other.receivePackets &&
                transmitBytes == other.transmitBytes &&
                transmitDrops == other.transmitDrops &&
                transmitErrors == other.transmitErrors &&
                transmitPackets == other.transmitPackets;

    }

    @Override
    public String toString() {
        return "OFMetricsPerPort [portId=" + portId +
                ", receivePackets=" + receivePackets +
                ", transmitPackets=" + transmitPackets +
                ", receiveBytes=" + receiveBytes +
                ", transmitBytes=" + transmitBytes +
                ", receiveDrops=" + receiveDrops +
                ", transmitDrops=" + transmitDrops +
                ", receiveErrors=" + receiveErrors +
                ", transmitErrors=" + transmitErrors +
                ", receiveFrameError=" + receiveFrameError +
                ", receiveOverRunError=" + receiveOverRunError +
                ", receiveCrcError=" + receiveCrcError +
                ", collisionCount=" + collisionCount + "]";
    }

}
