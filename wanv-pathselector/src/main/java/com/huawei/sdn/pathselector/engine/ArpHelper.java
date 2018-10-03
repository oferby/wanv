package com.huawei.sdn.pathselector.engine;

import java.net.InetAddress;

import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.packet.ARP;
import org.opendaylight.controller.sal.packet.Ethernet;
import org.opendaylight.controller.sal.packet.IDataPacketService;
import org.opendaylight.controller.sal.packet.RawPacket;
import org.opendaylight.controller.sal.utils.EtherTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.sdn.pathselector.tools.BeanFactory;
import com.huawei.sdn.commons.tools.NetUtils;

/**
 * Utility class to create ARP request/response with OpenDayLight
 */
public final class ArpHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArpHelper.class);

    final static byte[] BROADCAST = new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };

    final static byte[] ARP_BROADCAST = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };

    /**
     * utility class with only static methods
     */
    private ArpHelper() {
    }

    private static IDataPacketService getDataPacketService() {
        return (IDataPacketService) BeanFactory.getInstance().getBean(IDataPacketService.class);
    }

    /**
     * Create an Object ARP OpenDayLight
     * 
     * @param opCode
     *            REQUEST or RESPONSE (1 or 2)
     * @param senderMacAddress
     *            The sender physical Address
     * @param senderIP
     *            The sender IP
     * @param targetMacAddress
     *            The target physical Address
     * @param targetIP
     *            The target IP
     * @return The OpenDayLight ARP Object
     */
    public static ARP createARP(short opCode, byte[] senderMacAddress, byte[] senderIP, byte[] targetMacAddress,
            byte[] targetIP) {
        final ARP arp = new ARP();
        arp.setHardwareType(ARP.HW_TYPE_ETHERNET);
        arp.setProtocolType(EtherTypes.IPv4.shortValue());
        arp.setHardwareAddressLength((byte) NetUtils.MAC_ADDR_LENGTH_IN_BYTES);
        arp.setProtocolAddressLength((byte) 4);
        arp.setOpCode(opCode);
        arp.setSenderHardwareAddress(senderMacAddress);
        arp.setSenderProtocolAddress(senderIP);
        arp.setTargetHardwareAddress(targetMacAddress == null ? ARP_BROADCAST : targetMacAddress);
        arp.setTargetProtocolAddress(targetIP);
        return arp;
    }

    /**
     * create an OpenDayLight Ethernet Object (packet) containing an ARP
     * request/response
     * 
     * @param sourceMAC
     *            The physical Source Address
     * @param targetMAC
     *            The physical Target Address
     * @param arp
     *            The ARP Object
     * @return The Ethernet OpenDayLight Packet
     */
    public static Ethernet createEthernet(byte[] sourceMAC, byte[] targetMAC, ARP arp) {
        final Ethernet ethernet = new Ethernet();
        ethernet.setSourceMACAddress(sourceMAC);
        ethernet.setDestinationMACAddress(targetMAC == null ? BROADCAST : targetMAC);
        ethernet.setEtherType(EtherTypes.ARP.shortValue());
        ethernet.setPayload(arp);
        return ethernet;
    }

    /**
     * Send an ARP Reply to an OpenDayLight Node Connector
     * 
     * @param p
     *            The Node Connector
     * @param sMAC
     *            The physical Sender Address
     * @param sIP
     *            The Sender IP
     * @param tMAC
     *            The Target Physical Address
     * @param tIP
     *            The Target IP
     */
    public static void sendARPReply(NodeConnector p, byte[] sMAC, InetAddress sIP, byte[] tMAC, InetAddress tIP) {
        LOGGER.info("Send ARP Reply " + sIP.getHostAddress() + " (" + NetUtils.bytesToHexString(sMAC) + ") to "
                + tIP.getHostAddress() + " (" + NetUtils.bytesToHexString(tMAC) + ") - " + p.getID());
        final byte[] senderIP = sIP.getAddress();
        final byte[] targetIP = tIP.getAddress();
        final ARP arp = createARP(ARP.REPLY, sMAC, senderIP, tMAC, targetIP);

        final Ethernet ethernet = createEthernet(sMAC, tMAC, arp);

        final RawPacket destPkt = getDataPacketService().encodeDataPacket(ethernet);
        destPkt.setOutgoingNodeConnector(p);

        getDataPacketService().transmitDataPacket(destPkt);
    }

    /**
     * Send an ARP Request to an OpenDayLight Node Connector
     * 
     * @param p
     *            The Node Connector
     * @param sMAC
     *            The physical Sender Address
     * @param sIP
     *            The Sender IP
     * @param tIP
     *            The Target/Requested IP
     */
    public static void sendARPRequest(NodeConnector p, byte[] sMAC, InetAddress sIP, InetAddress tIP) {
        LOGGER.info("Send ARP Request who-has " + tIP.getHostAddress() + " tell " + sIP.getHostAddress() + ", "
                + NetUtils.bytesToHexString(sMAC) + " (" + p.getID() + ")");

        Ethernet ethernet = getArpPacket(sMAC, sIP, tIP);

        final RawPacket destPkt = getDataPacketService().encodeDataPacket(ethernet);

        destPkt.setOutgoingNodeConnector(p);

        getDataPacketService().transmitDataPacket(destPkt);
    }


    public static Ethernet getArpPacket(byte[] sMAC, InetAddress sIP, InetAddress tIP){

        final byte[] senderIP = sIP.getAddress();
        final byte[] targetIP = tIP.getAddress();
        final ARP arp = createARP(ARP.REQUEST, sMAC, senderIP, null, targetIP);

        final Ethernet ethernet = createEthernet(sMAC, null, arp);

        return ethernet;


    }




}
