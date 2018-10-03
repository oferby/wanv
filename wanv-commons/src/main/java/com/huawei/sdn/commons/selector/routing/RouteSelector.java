package com.huawei.sdn.commons.selector.routing;

import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.selector.routing.message.RouteInfo;
import org.apache.commons.net.util.SubnetUtils;

import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by oWX212574 on 6/9/2014.
 */
public interface RouteSelector {

    String getCidrForLocalAddress(String ipAddress);

    PSConnector getConnector(String srcIpAddress, String dstIpAddress, boolean checkStatus);

    boolean isConnected(String ipAddress);

    boolean isSelfIpAddress(String ipAddress);

    boolean isDefaultGateway(String ipAddress);

    void setDefaultRoutePortId(String ipAddress, int portId);

    InetAddress getLocalIpForSubnet(InetAddress address);

    Set<InetAddress> getLocalIpAddresses();

    Set<String> getLocalSubnetSet();

    Set<PSConnector> getAllConnectors();

    PSConnector getConnector(String id);

    void linkStatusChanged(String connectorId, boolean connected);

    List<PSConnector> getDefaultRoutes();

    List<String>getDefaultRoutesIdList();

    List<PSConnector> getTunnelConnectorList();

    List<String> getRemoteSiteIdList();

    Map<String, Set<String>>getConnectorIdPerTunnel();

    RouteInfo getFullRouteInfo();

    RouteInfo getRouteInfo();

}
