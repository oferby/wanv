package com.huawei.sdn.commons.config;

import com.huawei.sdn.commons.config.topology.Operation;
import com.huawei.sdn.commons.config.topology.Queue;
import com.huawei.sdn.commons.config.topology.Topology;

import java.util.Set;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 1/25/2015.
 */
public interface ConfigurationLoader {
    Topology load(String topologyXmlFile);

    Topology getTopology();

    String getReferenceUrl();

    int getTimeout();

    void setTopology(Topology topology);

    Operation getHeartBitOperation();

    Operation getDelayOperation();

    Set<Queue> getQueueSet();

    Queue getDefaultQueue();

    void setLocalIpAddress(String localIp, String localSubnet);

    void setDefaultGateway(String ip);

    void setTunnel(String tunnelId, String localIp, String remoteIp);

    void setStaticRoute(String destIp, String destSubnet, String nextHop);

}
