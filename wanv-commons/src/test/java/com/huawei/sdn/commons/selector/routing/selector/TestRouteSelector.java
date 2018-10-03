package com.huawei.sdn.commons.selector.routing.selector;

import com.huawei.sdn.commons.config.topology.TopologyServiceHelper;
import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.enums.ConnectorType;
import com.huawei.sdn.commons.selector.routing.RouteConfigurator;
import com.huawei.sdn.commons.selector.routing.RouteEntry;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import com.huawei.sdn.commons.selector.routing.RouteSelectorImpl;
import com.huawei.sdn.commons.selector.routing.message.RouteInfo;
import org.apache.commons.net.util.SubnetUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by oWX212574 on 12/22/2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RouteSelectorConfig.class})
public class TestRouteSelector {

    @Autowired
    private RouteSelector routeSelector;

    @Autowired
    private RouteConfigurator routeConfigurator;

    @Autowired
    private TopologyServiceHelper topologyServiceHelper;

    @Test
    public void testRouteEngine() {

        setup();

        PSConnector psConnector = routeSelector.getConnector("192.168.10.22", "192.168.20.11", false);

        assert psConnector != null && psConnector.getType() == ConnectorType.LOCAL;


        Set<String> reachableSubnetSet = routeSelector.getLocalSubnetSet();
        assert reachableSubnetSet.size() == 3;


//        check that we get teh right default gateway
        psConnector = routeSelector.getConnector("3.1.2.1", "1.1.2.1", false);

        assert psConnector.getType() == ConnectorType.REMOTE;


        psConnector = routeSelector.getConnector("192.168.20.12", "192.168.10.11", false);

        assert psConnector != null && psConnector.getType() == ConnectorType.REMOTE;


        psConnector = routeSelector.getConnector("192.168.20.11", "1.1.2.14", false);

        assert psConnector.getType() == ConnectorType.REMOTE;


        psConnector = routeSelector.getConnector("192.168.20.11", "192.168.30.1", false);

        assert psConnector.getType() == ConnectorType.REMOTE;
        assert psConnector.getNextHopIpAddress().equals("3.1.2.2");

//        psConnector = routeSelector.getConnector("192.168.20.11", "192.168.30.11", false);
//
//        assert psConnector.getType() == ConnectorType.REMOTE;
//        assert psConnector.getNextHopIpAddress().equals("4.1.2.2");





        addTunnelInfo();


        psConnector = routeSelector.getConnector("192.168.20.12", "192.168.10.11", false);

        assert psConnector != null && psConnector.getType() == ConnectorType.GRE;
        assert psConnector.getGroupId().equals("site1");

        //        check that we get teh right default gateway
        psConnector = routeSelector.getConnector("3.1.2.1", "1.1.2.1", false);

        assert psConnector.getType() == ConnectorType.REMOTE;


        psConnector = routeSelector.getConnector("192.168.20.11", "1.1.2.14", false);

        assert psConnector.getType() == ConnectorType.GRE;
        assert psConnector.getGroupId().equals("site1");

        psConnector = routeSelector.getConnector("192.168.20.11", "172.31.241.10", false);

        assert psConnector.getType() == ConnectorType.GRE;
        assert psConnector.getGroupId().equals("site1");


//        Set<RouteEntry> routeEntrySet = routeConfigurator.getRouteEntrySet();
//
//        assert routeEntrySet!=null;

        RouteInfo fullRouteInfo = routeSelector.getFullRouteInfo();

        assert fullRouteInfo!=null;

        routeConfigurator.setTunnelStaticRoute("site1","3.1.2.1/30");

    }


    private void addTunnelInfo() {

        List<PSConnector> tunnelConnectorList = routeSelector.getTunnelConnectorList();

        for (PSConnector connector : tunnelConnectorList) {

            Set<String> subnetSet = new HashSet<>();
            subnetSet.add("192.168.10.1/24");
            subnetSet.add("1.1.2.1/24");
            subnetSet.add("2.1.2.1/24");
            RouteInfo routeInfo = new RouteInfo();
            routeInfo.setSiteId("site1");
            routeInfo.setMacAddress("01:00:00:00:00:01");
            routeInfo.setSubnetSet(subnetSet);

            RouteInfo staticSubnet = new RouteInfo();
            staticSubnet.setSiteId("172.31.241.0/28");
            staticSubnet.getSubnetSet().add("172.31.241.0/28");

            routeInfo.getRemoteSiteSet().add(staticSubnet);

            RouteInfo sameSiteRoutingInfo = new RouteInfo();
            sameSiteRoutingInfo.setSiteId(topologyServiceHelper.getNodeId());
            sameSiteRoutingInfo.getSubnetSet().add("192.168.20.1/24");
            sameSiteRoutingInfo.getSubnetSet().add("3.1.2.1/30");
            sameSiteRoutingInfo.getSubnetSet().add("4.1.2.1/30");

            routeInfo.getRemoteSiteSet().add(sameSiteRoutingInfo);

            ((RouteSelectorImpl) routeSelector).setNewTunnelForSite(routeInfo, connector);

        }

    }


    private void setup() {

        routeConfigurator.setLocalIpAddress("192.168.20.1/24");
        routeConfigurator.setLocalIpAddress("3.1.2.1/30");
        routeConfigurator.setLocalIpAddress("4.1.2.1/30");

        routeConfigurator.setDefaultGateway("3.1.2.2");
        routeConfigurator.setDefaultGateway("4.1.2.2");


        routeConfigurator.setTunnel("11", "3.1.2.1", "1.1.2.1");
        routeConfigurator.setTunnel("12", "4.1.2.1", "2.1.2.1");

//
        routeConfigurator.setStaticRoute("192.168.30.1/24", "3.1.2.2");
//        routeConfigurator.setStaticRoute("192.168.30.11/32", "4.1.2.2");

    }


    @Test
    public void testSingleEndedTunnel(){

        routeConfigurator.setLocalIpAddress("192.168.10.1/24");
        routeConfigurator.setLocalIpAddress("1.1.2.1/30");
        routeConfigurator.setLocalIpAddress("2.1.2.1/30");

        routeConfigurator.setDefaultGateway("1.1.2.2");
        routeConfigurator.setDefaultGateway("2.1.2.2");


        routeConfigurator.setTunnel("11", "1.1.2.1", "4.1.2.1");
        routeConfigurator.setTunnel("12", "2.1.2.1", "4.1.2.1");


        PSConnector connector = routeSelector.getConnector("1.1.2.1", "4.1.2.1",false);

        assert connector.getLocalIPAddress().equals("1.1.2.1");

        connector = routeSelector.getConnector("2.1.2.1", "4.1.2.1",false);

        assert connector.getLocalIPAddress().equals("2.1.2.1");



    }




    @Test
    public void testStaticRoute() {

        routeConfigurator.setLocalIpAddress("192.168.20.1/24");
        routeConfigurator.setLocalIpAddress("3.1.2.1/24");
        routeConfigurator.setLocalIpAddress("4.1.2.1/24");

        routeConfigurator.setDefaultGateway("3.1.2.2");
        routeConfigurator.setDefaultGateway("4.1.2.2");

        routeConfigurator.setStaticRoute("192.168.30.11/32", "4.1.2.2");
        routeConfigurator.setStaticRoute("192.168.30.1/24", "3.1.2.2");


        PSConnector connector = routeSelector.getConnector("192.168.20.2", "192.168.30.1", false);

        assert connector.getNextHopIpAddress().equals("3.1.2.2");

        connector = routeSelector.getConnector("192.168.20.2", "192.168.30.11", false);

        assert connector.getNextHopIpAddress().equals("4.1.2.2");


    }


    @Test
    public void testSubnetUtils() {

        SubnetUtils subnetUtils = new SubnetUtils("192.168.30.9/32");

        subnetUtils.setInclusiveHostCount(true);

        assert subnetUtils.getInfo().isInRange("192.168.30.9");

    }

}
