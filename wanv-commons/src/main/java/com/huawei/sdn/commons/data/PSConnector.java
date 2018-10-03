package com.huawei.sdn.commons.data;

import com.huawei.sdn.commons.enums.ConnectorType;
import com.huawei.sdn.commons.tools.NetUtils;

import java.util.Arrays;

/**
 * Internal Object that Represents a Switch Port with all the configuration/know
 * data about this port.
 */
public class PSConnector {

    /**
     * Unique id of the connector
     */
    private String id;

    private String groupId = "";

    private String localIPAddress;

    private String destinationAddress;

    /**
     * IP of the connector
     */
    private String nextHopIpAddress;

//    used for Y1731
    private byte[] localMacAddress = NetUtils.hexStringToByteArray("01:00:00:00:00:01");

//    used for Y1731
    private byte[] remoteMacAddress = NetUtils.hexStringToByteArray("01:00:00:00:00:02");

    /**
     * the connector type
     */
    private ConnectorType type;

    /**
     * the connector type
     */
    private boolean active = true;

    private boolean movable = false;

    private ConnectorStatistics connectorStatistics = new ConnectorStatistics();

    public PSConnector(String id) {
        this.id = id;
    }

    public PSConnector(String id, ConnectorType type) {
        super();
        this.id = id;
        this.type = type;
        this.active = true;
    }

    public PSConnector(String id, String nextHopIpAddress, ConnectorType type) {
        super();
        this.id = id;
        this.nextHopIpAddress = nextHopIpAddress;
        this.type = type;
        this.active = true;
    }

    public PSConnector(PSConnector connector) {
        this.id = connector.id;
        this.groupId = connector.groupId;
        this.localIPAddress = connector.localIPAddress;
        this.destinationAddress = connector.destinationAddress;
        this.nextHopIpAddress = connector.nextHopIpAddress;
        this.localMacAddress = connector.localMacAddress;
        this.remoteMacAddress = connector.remoteMacAddress;
        this.type = connector.type;
        this.active = connector.active;
        this.movable = connector.movable;
        this.connectorStatistics = connector.connectorStatistics;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getNextHopIpAddress() {
        return nextHopIpAddress;
    }

    public void setNextHopIpAddress(String nextHopIpAddress) {
        this.nextHopIpAddress = nextHopIpAddress;
    }

    public byte[] getLocalMacAddress() {
        return localMacAddress;
    }

    public String getLocalMacString(){
        return NetUtils.bytesToHexString(localMacAddress);
    }

    public void setLocalMacAddress(byte[] localMacAddress) {
        this.localMacAddress = new byte[localMacAddress.length];
        System.arraycopy(localMacAddress,0,this.localMacAddress,0,localMacAddress.length);

    }

    public String getRemoteMacString(){
        return NetUtils.bytesToHexString(remoteMacAddress);
    }

    public byte[] getRemoteMacAddress() {
        return remoteMacAddress;
    }

    public void setRemoteMacAddress(byte[] remoteMacAddress) {
        this.remoteMacAddress = new byte[remoteMacAddress.length];
        System.arraycopy(remoteMacAddress,0,this.remoteMacAddress,0,remoteMacAddress.length);
    }

    public ConnectorType getType() {
        return type;
    }

    public void setType(ConnectorType type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setMovable(boolean movable){
        this.movable = movable;
    }

    public boolean isMovable() {
        return movable;
    }

    public String getLocalIPAddress() {
        return localIPAddress;
    }

    public void setLocalIPAddress(String localIPAddress) {
        this.localIPAddress = localIPAddress;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
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
        // the equals only takes in account the id.
        final PSConnector other = (PSConnector) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    public ConnectorStatistics getConnectorStatistics() {
        return connectorStatistics;
    }

    public void setConnectorStatistics(ConnectorStatistics connectorStatistics) {
        this.connectorStatistics = connectorStatistics;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    @Override
    public String toString() {
        return "PSConnector{" +
                "id='" + id + '\'' +
                ", groupId=" + groupId +
                ", localAddress=" + localIPAddress +
                ", nextHopIpAddress='" + nextHopIpAddress + '\'' +
                ", type=" + type +
                ", active=" + active +
                ", movable=" + movable +
                '}';
    }
}
