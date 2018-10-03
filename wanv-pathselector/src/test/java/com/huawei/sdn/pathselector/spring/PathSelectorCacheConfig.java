package com.huawei.sdn.pathselector.spring;

import com.huawei.sdn.commons.cache.ExpirableCache;
import com.huawei.sdn.commons.cache.ExpirableCacheImpl;
import com.huawei.sdn.commons.data.MacAddress;
import com.huawei.sdn.commons.data.PSFlow;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.packet.RawPacket;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/2/14
 * Time: 10:06 AM
 * To change this template use File | Settings | File Templates.
 */
//@Configuration
public class PathSelectorCacheConfig {

    @Bean
    @Qualifier("macNodeCache")
    public ExpirableCache<MacAddress, NodeConnector> getMacNodeCache() {
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
    @Qualifier("waitingFlows")
    public ExpirableCache<InetAddress, PSFlow> getWaitingFlows(){

        ExpirableCache<InetAddress, PSFlow> waitingFlows = new ExpirableCacheImpl<>();
        waitingFlows.setExpirationTime(30000);
        return waitingFlows;

    }

}
