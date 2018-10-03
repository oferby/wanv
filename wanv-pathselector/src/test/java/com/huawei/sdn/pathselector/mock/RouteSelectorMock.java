package com.huawei.sdn.pathselector.mock;

import com.huawei.sdn.commons.selector.routing.RouteSelector;
import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.selector.routing.message.RouteInfo;
import org.apache.commons.net.util.SubnetUtils;

import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/2/14
 * Time: 10:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class RouteSelectorMock implements RouteSelector{


    @Override
    public String getCidrForLocalAddress(String ipAddress) {
        return null;
    }

    @Override
    public PSConnector getConnector(String srcIpAddress, String dstIpAddress, boolean checkStatus) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isConnected(String ipAddress) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isSelfIpAddress(String ipAddress) {
        return false;
    }

    @Override
    public boolean isDefaultGateway(String ipAddress) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setDefaultRoutePortId(String ipAddress, int portId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public InetAddress getLocalIpForSubnet(InetAddress address) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<InetAddress> getLocalIpAddresses() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<String> getLocalSubnetSet() {
        return null;
    }

    @Override
    public Set<PSConnector> getAllConnectors() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PSConnector getConnector(String id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void linkStatusChanged(String connectorId, boolean connected) {
        //To change body of implemented methods use File | Settings | File Templates.
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
