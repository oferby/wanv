package com.huawei.sdn.pathselector.mock;

import com.huawei.sdn.pathselector.engine.ArpHelperWrapper;
import org.opendaylight.controller.sal.core.NodeConnector;

import java.net.InetAddress;

/**
 * Created by root on 7/1/14.
 */
public class ArpHelpWrapperMock implements ArpHelperWrapper {
    @Override
    public void sendARPRequest(NodeConnector p, byte[] sMAC, InetAddress sIP, InetAddress tIP) {

    }

    @Override
    public void sendARPReply(NodeConnector p, byte[] sMAC, InetAddress sIP, byte[] tMAC, InetAddress tIP) {

    }
}
