package com.huawei.sdn.commons.selector.routing;

import com.huawei.sdn.commons.data.PSConnector;

import java.util.Map;
import java.util.Set;

/**
 * Created by oWX212574 on 12/21/2014.
 */
public interface RouteConfigurator {

    PSConnector setLocalIpAddress(String cidr);

    PSConnector setStaticRoute(String destCidr, String nextHopeIpAddress);

    void setTunnelStaticRoute(String siteId, String destCidr);

    PSConnector setTunnel(String tunnelId, String localIpAddress, String remoteIpAddress);

    void setSiteToTunnel(String siteId, String tunnelId);

    PSConnector setDefaultGateway(String ipAddress);

    Set<RouteEntry>getRouteEntrySet();



}
