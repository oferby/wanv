package com.huawei.sdn.pathselector.odl.topology;

import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.packet.Ethernet;
import org.opendaylight.controller.sal.packet.Packet;

import java.net.Inet4Address;
import java.net.InetAddress;

/**
 * Created by oWX212574 on 12/7/2014.
 */
public interface TopologyHandler {

    void addHost(NodeConnector inConnector, byte[] macAddress, InetAddress srcIpAddress);


}
