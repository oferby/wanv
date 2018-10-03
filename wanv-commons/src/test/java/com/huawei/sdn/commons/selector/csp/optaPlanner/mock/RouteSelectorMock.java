package com.huawei.sdn.commons.selector.csp.optaPlanner.mock;

import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.enums.ConnectorType;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import com.huawei.sdn.commons.selector.routing.message.RouteInfo;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 1/25/2015.
 */
public class RouteSelectorMock implements RouteSelector {

    private Set<PSConnector>connectorSet=new HashSet<>();

    public RouteSelectorMock() {

        PSConnector connector = new PSConnector("1", ConnectorType.GRE);
        connector.setGroupId("g1");
        connectorSet.add(connector);

        connector = new PSConnector("2", ConnectorType.GRE);
        connector.setGroupId("g1");
        connectorSet.add(connector);

        connector = new PSConnector("3", ConnectorType.GRE);
        connector.setGroupId("g2");
        connectorSet.add(connector);

        connector = new PSConnector("4", ConnectorType.GRE);
        connector.setGroupId("g2");
        connectorSet.add(connector);

        connector = new PSConnector("5", ConnectorType.REMOTE);
        connectorSet.add(connector);

        connector = new PSConnector("6", ConnectorType.REMOTE);
        connectorSet.add(connector);

        connector = new PSConnector("7", ConnectorType.LOCAL);
        connectorSet.add(connector);

    }

    @Override
    public String getCidrForLocalAddress(String ipAddress) {
        return null;
    }

    @Override
    public PSConnector getConnector(String srcIpAddress, String dstIpAddress, boolean checkStatus) {
        return null;
    }

    @Override
    public boolean isConnected(String ipAddress) {
        return false;
    }

    @Override
    public boolean isSelfIpAddress(String ipAddress) {
        return false;
    }

    @Override
    public boolean isDefaultGateway(String ipAddress) {
        return false;
    }

    @Override
    public void setDefaultRoutePortId(String ipAddress, int portId) {

    }

    @Override
    public InetAddress getLocalIpForSubnet(InetAddress address) {
        return null;
    }

    @Override
    public Set<InetAddress> getLocalIpAddresses() {
        return null;
    }

    @Override
    public Set<String> getLocalSubnetSet() {
        return null;
    }

    @Override
    public Set<PSConnector> getAllConnectors() {
        return connectorSet;
    }

    @Override
    public PSConnector getConnector(String id) {
        return null;
    }

    @Override
    public void linkStatusChanged(String connectorId, boolean connected) {

    }

    @Override
    public List<PSConnector> getDefaultRoutes() {
        return null;
    }

    @Override
    public List<String> getDefaultRoutesIdList() {
        return null;
    }

    @Override
    public List<PSConnector> getTunnelConnectorList() {
        return null;
    }

    @Override
    public List<String> getRemoteSiteIdList() {
        return null;
    }

    @Override
    public Map<String, Set<String>> getConnectorIdPerTunnel() {
        return null;
    }

    @Override
    public RouteInfo getFullRouteInfo() {
        return null;
    }

    @Override
    public RouteInfo getRouteInfo() {
        return null;
    }
}
