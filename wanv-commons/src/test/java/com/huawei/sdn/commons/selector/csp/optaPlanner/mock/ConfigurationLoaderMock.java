package com.huawei.sdn.commons.selector.csp.optaPlanner.mock;

import com.huawei.sdn.commons.config.ConfigurationLoader;
import com.huawei.sdn.commons.config.topology.Operation;
import com.huawei.sdn.commons.config.topology.Queue;
import com.huawei.sdn.commons.config.topology.Topology;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 1/25/2015.
 */
public class ConfigurationLoaderMock implements ConfigurationLoader {

    @Override
    public Topology load(String topologyXmlFile) {
        return null;
    }

    @Override
    public Topology getTopology() {
        return null;
    }

    @Override
    public String getReferenceUrl() {
        return null;
    }

    @Override
    public int getTimeout() {
        return 0;
    }

    @Override
    public void setTopology(Topology topology) {

    }

    @Override
    public Operation getHeartBitOperation() {
        return null;
    }

    @Override
    public Operation getDelayOperation() {
        return null;
    }

    @Override
    public Set<Queue> getQueueSet(){
        return new HashSet<>();
    }

    @Override
    public Queue getDefaultQueue() {
        return null;
    }

    @Override
    public void setLocalIpAddress(String localIp, String localSubnet) {

    }

    @Override
    public void setDefaultGateway(String ip) {

    }

    @Override
    public void setTunnel(String tunnelId, String localIp, String remoteIp) {

    }

    @Override
    public void setStaticRoute(String destIp, String destSubnet, String nextHop) {

    }


}
