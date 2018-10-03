package com.huawei.sdn.pathselector.spring;

import com.huawei.sdn.commons.cache.ExpirableCache;
import com.huawei.sdn.commons.cache.ExpirableCacheImpl;
import com.huawei.sdn.commons.data.MacAddress;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import com.huawei.sdn.pathselector.mock.NodeConnectorHandlerMock;
import com.huawei.sdn.pathselector.mock.ArpHandlerMock;
import com.huawei.sdn.pathselector.mock.RouteSelectorMock;
import com.huawei.sdn.pathselector.odl.ArpHandler;
import com.huawei.sdn.pathselector.odl.L2.PacketHandler;
import com.huawei.sdn.pathselector.odl.L2.PacketHandlerImpl;
import com.huawei.sdn.pathselector.odl.L2.NodeConnectorHandler;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.packet.RawPacket;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/1/14
 * Time: 2:08 PM
 * To change this template use File | Settings | File Templates.
 */
@Configuration
//@ComponentScan("com.huawei.sdn.pathselector")
public class TestContext {

    @Bean
    PacketHandler getL2PacketHandler(){
        return new PacketHandlerImpl();
    }

    @Bean
    @Qualifier("macNodeCache")
    public ExpirableCache<MacAddress, NodeConnector> getPortCache() {
        ExpirableCache<MacAddress, NodeConnector> portHandler = new ExpirableCacheImpl<>();
        portHandler.setExpirationTime(30000);
        return portHandler;
    }

    @Bean
    @Qualifier("inetPacketCache")
    public ExpirableCache<InetAddress, RawPacket> getPacketCache() {
        ExpirableCache<InetAddress, RawPacket> portHandler = new ExpirableCacheImpl<>();
        portHandler.setExpirationTime(30000);
        return portHandler;
    }

    @Bean
    public RouteSelector getRouteSelector(){
        return new RouteSelectorMock();
    }

    @Bean
    public ArpHandler getArpHandler(){
        return new ArpHandlerMock();
    }

    @Bean
    public NodeConnectorHandler getNodeConnectorHandler(){
        return new NodeConnectorHandlerMock();
    }



}
