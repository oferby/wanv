package com.huawei.sdn.pathselector.engine;

import org.opendaylight.controller.sal.core.NodeConnector;

import java.net.InetAddress;

/**
 * Created by root on 7/1/14.
 */
public interface ArpHelperWrapper {
    void sendARPRequest(NodeConnector p, byte[] sMAC, InetAddress sIP, InetAddress tIP);

    void sendARPReply(NodeConnector p, byte[] sMAC, InetAddress sIP, byte[] tMAC, InetAddress tIP);
}
