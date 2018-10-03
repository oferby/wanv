package com.huawei.sdn.pathselector.engine;

import org.opendaylight.controller.sal.core.NodeConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.net.InetAddress;

/**
 * Created by root on 7/1/14.
 */
@Controller("arpHelperWrapper")
public class ArpHelperWrapperImpl implements ArpHelperWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArpHelperWrapperImpl.class);

    @Override
    public void sendARPRequest(NodeConnector p, byte[] sMAC, InetAddress sIP, InetAddress tIP) {

        ArpHelper.sendARPRequest(p, sMAC, sIP, tIP);
    }

    @Override
    public void sendARPReply(NodeConnector p, byte[] sMAC, InetAddress sIP, byte[] tMAC, InetAddress tIP) {
        LOGGER.info("sending ARP Reply to " + tIP.getHostAddress());
        ArpHelper.sendARPReply(p,sMAC,sIP,tMAC,tIP);
    }
}
