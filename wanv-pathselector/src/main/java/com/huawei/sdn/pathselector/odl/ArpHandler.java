package com.huawei.sdn.pathselector.odl;

import org.opendaylight.controller.sal.packet.ARP;
import org.opendaylight.controller.sal.packet.Packet;
import org.opendaylight.controller.sal.packet.RawPacket;
import org.opendaylight.controller.switchmanager.ISwitchManager;

import java.net.InetAddress;

/**
 * Created by root on 6/30/14.
 */
public interface ArpHandler {

    void sendARPRequest(InetAddress dst);
    void sendARPRequest(byte[] sMAC, InetAddress sIP, InetAddress tIP);
    void sendARPReply(RawPacket rawPacket, ARP packet, byte[] sMac);
    void savePacketAndSendArp(RawPacket rawPacket, Packet packet, InetAddress dst);
    RawPacket getAndRemoveAfterArp(InetAddress address);
}
