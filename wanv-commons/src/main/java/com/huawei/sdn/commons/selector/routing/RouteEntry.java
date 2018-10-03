package com.huawei.sdn.commons.selector.routing;

import com.google.common.collect.Sets;
import com.huawei.sdn.commons.data.PSConnector;
import org.apache.commons.net.util.SubnetUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by oWX212574 on 12/18/2014.
 */
public class RouteEntry {

    private SubnetUtils routeInfo;
    private Set<PSConnector> connectorSet;

    public RouteEntry(SubnetUtils routeInfo, Set<PSConnector> connectors) {
        this.routeInfo = routeInfo;
        this.connectorSet = Sets.newHashSet(connectors);
    }

    public RouteEntry(SubnetUtils routeInfo) {
        this.routeInfo = routeInfo;
        this.connectorSet = new HashSet<>();
    }

    public SubnetUtils getRouteInfo() {
        return routeInfo;
    }

    public Set<PSConnector> getConnectorSet() {
        return connectorSet;
    }

    public void addConnector(PSConnector psConnector){
        connectorSet.add(psConnector);
    }

    public int getNetworkAddress(){
        return routeInfo.getInfo().asInteger(routeInfo.getInfo().getNetworkAddress());
    }



}
