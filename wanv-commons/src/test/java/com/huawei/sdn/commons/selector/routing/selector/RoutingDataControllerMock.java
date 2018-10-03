package com.huawei.sdn.commons.selector.routing.selector;

import com.huawei.sdn.commons.db.RoutingDataController;
import com.huawei.sdn.commons.db.model.Gateway;
import com.huawei.sdn.commons.db.model.LocalIpAddress;
import com.huawei.sdn.commons.db.model.StaticRoute;
import com.huawei.sdn.commons.db.model.Tunnel;

import java.util.List;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 4/26/2015.
 */
public class RoutingDataControllerMock implements RoutingDataController {
    @Override
    public List<LocalIpAddress> getLocalIpAddressList() {
        return null;
    }

    @Override
    public void saveLocalIpAddress(String ipAddress, String subnet) {

    }

    @Override
    public void deleteLocalIpAddress(int id) {

    }

    @Override
    public List<Gateway> getGatewayList() {
        return null;
    }

    @Override
    public void saveDefaultGateway(String ipAddress) {

    }

    @Override
    public void deleteDefaultGateway(int id) {

    }

    @Override
    public List<Tunnel> getTunnelList() {
        return null;
    }

    @Override
    public void saveTunnel(String tunnelId, String localIp, String remoteIp) {

    }

    @Override
    public void deleteTunnel(int id) {

    }

    @Override
    public List<StaticRoute> getStaticRouteList() {
        return null;
    }

    @Override
    public void saveStaticRoute(String destIp, String destSubnet, String nextHop) {

    }

    @Override
    public void deleteStaticRoute(int id) {

    }
}
