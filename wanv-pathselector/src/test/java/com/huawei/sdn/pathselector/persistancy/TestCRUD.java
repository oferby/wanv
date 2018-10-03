package com.huawei.sdn.pathselector.persistancy;

import com.huawei.sdn.commons.db.RoutingDataController;
import com.huawei.sdn.pathselector.db.RoutingDataControllerImpl;
import com.huawei.sdn.commons.db.model.Gateway;
import com.huawei.sdn.commons.db.model.LocalIpAddress;
import com.huawei.sdn.commons.db.model.StaticRoute;
import com.huawei.sdn.commons.db.model.Tunnel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 3/10/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestDbConfiguration.class})
public class TestCRUD {

    @Autowired
    private RoutingDataController routingDataController;

    @Test
    public void testIpAddresses() {

        String ipAddress = "1.1.1.1";
        String subnet = "24";

        ((RoutingDataControllerImpl) routingDataController).setup();

        routingDataController.saveLocalIpAddress(ipAddress, subnet);

        List<LocalIpAddress> localIpAddressList = routingDataController.getLocalIpAddressList();

        assert localIpAddressList != null && localIpAddressList.size() == 1;

        LocalIpAddress localIpAddress = localIpAddressList.get(0);

        assert localIpAddress.getIpAddress().equals(ipAddress) && localIpAddress.getSubnet().equals(subnet);

        routingDataController.deleteLocalIpAddress(localIpAddress.getId());

        localIpAddressList = routingDataController.getLocalIpAddressList();

        assert localIpAddressList.size() == 0;


    }


    @Test
    public void testGatewaysCRUD() {

        String ipAddress = "1.1.1.2";

        routingDataController.saveDefaultGateway(ipAddress);

        List<Gateway> gatewayList = routingDataController.getGatewayList();

        assert gatewayList != null && gatewayList.size() == 1 && gatewayList.get(0).getIp_address().equals(ipAddress);

        routingDataController.deleteDefaultGateway(gatewayList.get(0).getId());

        gatewayList = routingDataController.getGatewayList();

        assert gatewayList != null && gatewayList.size() == 0;

    }


    @Test
    public void testTunnelCRUD() {

        String id = "11";
        String localIp = "1.1.1.1";
        String remoteIp = "2.2.2.2";

        routingDataController.saveTunnel(id,localIp,remoteIp);


        List<Tunnel> tunnelList = routingDataController.getTunnelList();

        assert tunnelList.size()==1;

        Tunnel tunnel = tunnelList.get(0);

        assert tunnel.getLocalIp().equals(localIp) && tunnel.getRemoteIp().equals(remoteIp);

        routingDataController.deleteTunnel(Integer.parseInt(id));

        tunnelList = routingDataController.getTunnelList();

        assert tunnelList.size()==0;


    }


    @Test
    public void testStaticRouteCRUD(){

        String destIp = "3.3.3.3";
        String destSubnet = "24";
        String nextHop = "3.3.3.254";

        routingDataController.saveStaticRoute(destIp,destSubnet,nextHop);

        List<StaticRoute> staticRouteList = routingDataController.getStaticRouteList();

        assert staticRouteList.size()==1;

        StaticRoute staticRoute = staticRouteList.get(0);

        assert staticRoute.getDestIp().equals(destIp) && staticRoute.getDestSubnet().equals(destSubnet) && staticRoute.getNextHop().equals(nextHop);

        routingDataController.deleteStaticRoute(staticRoute.getId());

        staticRouteList = routingDataController.getStaticRouteList();

        assert staticRouteList.size()==0;


    }


}
