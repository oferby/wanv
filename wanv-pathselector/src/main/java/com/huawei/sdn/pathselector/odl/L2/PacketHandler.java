package com.huawei.sdn.pathselector.odl.L2;

import com.huawei.sdn.commons.data.PSFlow;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerService;
import org.opendaylight.controller.sal.packet.Ethernet;
import org.opendaylight.controller.sal.packet.IDataPacketService;
import org.opendaylight.controller.sal.packet.Packet;
import org.opendaylight.controller.sal.packet.RawPacket;
import org.opendaylight.controller.switchmanager.ISwitchManager;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/1/14
 * Time: 1:27 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PacketHandler {

    boolean handlePacket(RawPacket inPkt, Ethernet formattedPak);

    void enablePathSelector(boolean enable);

    boolean isPathSelectorEnabled();

    void sendArpToDefaultGateways();


}
