package com.huawei.sdn.commons.selector.routing.message;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by oWX212574 on 12/22/2014.
 */
public class RouteInfo {

    private String siteId;

    private String macAddress;

    private Set<String> subnetSet = new HashSet<>();

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    private Set<RouteInfo> remoteSiteSet = new HashSet<>();

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteId() {
        return siteId;
    }

    public Set<String> getSubnetSet() {
        return subnetSet;
    }

    public void setSubnetSet(Set<String> subnetSet) {
        this.subnetSet = subnetSet;
    }

    public Set<RouteInfo> getRemoteSiteSet() {
        return remoteSiteSet;
    }

    public void addRemoteSiteRouteInfo(RouteInfo routeInfo){
        remoteSiteSet.add(routeInfo);
    }

    @Override
    public String toString() {
        return "RouteInfo{" +
                "mac='" + macAddress +
                "', siteId='" + siteId + '\'' +
                ", subnetSet=" + subnetSet +
                '}';
    }
}
