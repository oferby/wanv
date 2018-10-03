package com.huawei.sdn.pathselector.entities;

import java.util.Set;

import org.opendaylight.controller.sal.packet.Ethernet;
import org.opendaylight.controller.sal.packet.IEEE8021Q;
import org.opendaylight.controller.sal.packet.IPv4;
import org.opendaylight.controller.sal.packet.Packet;
import org.opendaylight.controller.sal.packet.TCP;
import org.opendaylight.controller.sal.packet.UDP;

import com.huawei.sdn.commons.tools.NetUtils;
import com.huawei.sdn.commons.data.PSPacketIn;
import com.huawei.sdn.commons.data.PSPacketIn.OfbMatchFields;

/**
 * The Packet Path Selector creator helper
 */
public class ODLPSPacketInCreator {

    private ODLPSPacketInCreator() {
    }

    /**
     * update the Packet In Internal Object with an OpenDaylight Packet Object
     * 
     * @param packet
     *            The OpenDayLight Payload Object
     * @param psPacket
     *            the Packet In Internal Object to update
     */
    private static void updateFields(Packet packet, PSPacketIn psPacket) {
        if (packet instanceof Ethernet) {
            // dlSrc
            psPacket.setDlSrc(((Ethernet) packet).getSourceMACAddress());
            // dlDst
            psPacket.setDlSrc(((Ethernet) packet).getDestinationMACAddress());
            // dlType
            psPacket.setDlType(((Ethernet) packet).getEtherType());
        }
        if (packet instanceof IEEE8021Q) {
            // dlVlan
            psPacket.setDlVlan(((IEEE8021Q) packet).getVid());
            // dlVlanPriority
            psPacket.setDlVlanPriority(((IEEE8021Q) packet).getPcp());
        }
        if (packet instanceof UDP) {
            // tpSrc
            psPacket.setTpSrc(((UDP) packet).getSourcePort());
            // tpDst
            psPacket.setTpDst(((UDP) packet).getDestinationPort());
        }
        if (packet instanceof TCP) {
            // tpSrc
            psPacket.setTpSrc(((TCP) packet).getSourcePort());
            // tpDst
            psPacket.setTpDst(((TCP) packet).getDestinationPort());
        }
        if (packet instanceof IPv4) {
            // nwProto
            psPacket.setNwProto(((IPv4) packet).getProtocol());
            // nwSrc
            psPacket.setNwSrc(NetUtils.getInetAddress(((IPv4) packet).getSourceAddress()));
            // nwDst
            psPacket.setNwDst(NetUtils.getInetAddress(((IPv4) packet).getDestinationAddress()));
            // nwTOS
            psPacket.setNwTOS(((IPv4) packet).getECN());
        }
    }

    /**
     * get all member information from IPv4 Packet OpenDayLight Object
     * 
     * @param nextPakA
     *            The OpenDayLight Object
     * @param inPortId
     *            The incoming port Id
     * @param ofbMatchFields
     *            The Match Fields parameter configuration
     * @return
     */
    public static PSPacketIn create(IPv4 nextPakA, String inPortId, Set<OfbMatchFields> ofbMatchFields) {
        final PSPacketIn psPacket = new PSPacketIn(ofbMatchFields);
        // inPort
        psPacket.setInPort(inPortId);
        // nextPakA
        updateFields(nextPakA, psPacket);
        // parents data
        Packet current = nextPakA.getParent();
        while (current != null) {
            updateFields(current, psPacket);
            current = current.getParent();
        }
        // all payload data
        current = nextPakA.getPayload();
        while (current != null) {
            updateFields(current, psPacket);
            current = current.getPayload();
        }
        return psPacket;
    }

}
