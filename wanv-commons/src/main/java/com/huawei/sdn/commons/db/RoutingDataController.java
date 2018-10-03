package com.huawei.sdn.commons.db;

import com.huawei.sdn.commons.db.model.Gateway;
import com.huawei.sdn.commons.db.model.LocalIpAddress;
import com.huawei.sdn.commons.db.model.StaticRoute;
import com.huawei.sdn.commons.db.model.Tunnel;

import java.util.List;

/**
 * Created by oWX212574 on 1/5/2015.
 */

public interface RoutingDataController {

    List<LocalIpAddress>getLocalIpAddressList();
    void saveLocalIpAddress(String ipAddress, String subnet);
    void deleteLocalIpAddress(int id);
    List<Gateway>getGatewayList();
    void saveDefaultGateway(String ipAddress);
    void deleteDefaultGateway(int id);
    List<Tunnel>getTunnelList();
    void saveTunnel(String tunnelId, String localIp, String remoteIp);
    void deleteTunnel(int id);
    List<StaticRoute>getStaticRouteList();
    void saveStaticRoute(String destIp, String destSubnet,String nextHop);
    void deleteStaticRoute(int id);

}
