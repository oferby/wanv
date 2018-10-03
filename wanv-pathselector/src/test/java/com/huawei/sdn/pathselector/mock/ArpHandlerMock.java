package com.huawei.sdn.pathselector.mock;

import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.pathselector.odl.ArpHandler;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.packet.ARP;
import org.opendaylight.controller.sal.packet.Packet;
import org.opendaylight.controller.sal.packet.RawPacket;
import org.opendaylight.controller.switchmanager.ISwitchManager;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/2/14
 * Time: 10:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class ArpHandlerMock implements ArpHandler{

    @Override
    public void sendARPRequest(InetAddress dst) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void sendARPRequest(byte[] sMAC, InetAddress sIP, InetAddress tIP) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void sendARPReply(RawPacket rawPacket, ARP packet, byte[] sMac) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void savePacketAndSendArp(RawPacket rawPacket, Packet packet, InetAddress dst) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public RawPacket getAndRemoveAfterArp(InetAddress address) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
