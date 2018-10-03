package com.huawei.sdn.pathselector.arp;

import com.huawei.sdn.commons.cache.ExpirableCache;
import com.huawei.sdn.commons.cache.ExpirableCacheImpl;
import com.huawei.sdn.commons.data.MacAddress;
import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.pathselector.engine.ArpHelperWrapper;
import com.huawei.sdn.pathselector.mock.ArpHelpWrapperMock;
import com.huawei.sdn.pathselector.mock.NodeConnectorHandlerMock;
import com.huawei.sdn.pathselector.odl.ArpHandler;
import com.huawei.sdn.pathselector.odl.ArpHandlerImpl;
import com.huawei.sdn.pathselector.odl.L2.NodeConnectorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;

/**
 * Created by root on 7/1/14.
 */
@Configuration
public class ArpHandlerContext {

    @Bean
    public ArpHandler getArpManager() {
        ArpHandler res = new ArpHandlerImpl();
        return res;
    }

    @Bean(name = "ipToMac")
    public ExpirableCache<InetAddress, MacAddress> getIpToMac() {
        return new ExpirableCacheImpl<>();
    }

    @Bean(name = "macToConnector")
    public ExpirableCache<MacAddress, PSConnector> getMacToConnector() {
        return new ExpirableCacheImpl<>();
    }

    @Bean
    public ArpHelperWrapper getArpHelperWrapper() {
        return new ArpHelpWrapperMock();
    }

    @Bean(name = "waitingFlows")
    public ExpirableCache<InetAddress, PSFlow> getWaitingFlows() {
        return new ExpirableCacheImpl<>();
    }

    @Bean
    public NodeConnectorHandler getNodeConnectorHandler(){
        return new NodeConnectorHandlerMock();
    }

}
