package com.huawei.sdn.pathselector.odl;

import com.huawei.sdn.commons.cache.ExpirableCache;
import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import com.huawei.sdn.pathselector.engine.ArpHelper;
import com.huawei.sdn.pathselector.engine.ArpHelperWrapper;
import com.huawei.sdn.pathselector.odl.L2.NodeConnectorHandler;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.packet.ARP;
import org.opendaylight.controller.sal.packet.Packet;
import org.opendaylight.controller.sal.packet.RawPacket;
import org.opendaylight.controller.switchmanager.ISwitchManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Amir Kost on 6/30/14.
 */
@Controller("arpHandler")
public class ArpHandlerImpl implements ArpHandler {

    private final int EXPIRATION_TIME_MS = 30000;
    private final int WAITING_FLOWS_EXPIRATION_TIME_MS = 5000;
    private final int EXPIRED_FLOWS_DELETE_INTERVAL = 2000;

    @Autowired
    private ArpHelperWrapper arpHelperWrapper;
    @Autowired
    @Qualifier("waitingFlows")
    private ExpirableCache<InetAddress, PSFlow> waitingFlows;
    @Autowired
    @Qualifier("inetPacketCache")
    private ExpirableCache<InetAddress, RawPacket> inetPacketCache;
    @Autowired
    private NodeConnectorHandler nodeConnectorHandler;
    @Autowired
    private RouteSelector routeSelector;

    @Autowired
    private ServiceHelper serviceHelper;

    @Override
    public void sendARPRequest(InetAddress dst) {

        ISwitchManager switchManager = serviceHelper.getSwitchManager();

        Node node = switchManager.getNodes().iterator().next();
        byte[] nodeMAC = switchManager.getNodeMAC(node);

        InetAddress localIp = routeSelector.getLocalIpForSubnet(dst);

        Set<NodeConnector> physicalNodeConnectors = switchManager.getPhysicalNodeConnectors(node);

        for (NodeConnector physicalNodeConnector : physicalNodeConnectors) {
            String name = switchManager.getNodeConnectorProp(physicalNodeConnector, "name").getStringValue();
            if(name.contains("eth")||name.contains("veth")) {
                arpHelperWrapper.sendARPRequest(physicalNodeConnector, nodeMAC, localIp, dst);
            }

        }

    }

    @Override
    public void sendARPRequest(byte[] sMAC, InetAddress sIP, InetAddress tIP) {

        ISwitchManager switchManager = serviceHelper.getSwitchManager();

        Node node = switchManager.getNodes().iterator().next();

        Set<NodeConnector> nodeConnectors = switchManager.getPhysicalNodeConnectors(node);

        for (NodeConnector nodeConnector : nodeConnectors) {
            String name = switchManager.getNodeConnectorProp(nodeConnector, "name").getStringValue();
            if(name.startsWith("eth")||name.startsWith("veth")) {
                ArpHelper.sendARPRequest(nodeConnector, sMAC, sIP, tIP);
            }

        }

    }

    @Override
    public void sendARPReply(RawPacket rawPacket, ARP packet, byte[] sMac) {

        try {

            arpHelperWrapper.sendARPReply(rawPacket.getIncomingNodeConnector(),
                    sMac,
                    InetAddress.getByAddress(packet.getTargetProtocolAddress()),
                    packet.getSenderHardwareAddress(),
                    InetAddress.getByAddress(packet.getSenderProtocolAddress()));

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void savePacketAndSendArp(RawPacket rawPacket, Packet packet, InetAddress dst) {

        inetPacketCache.addEntry(dst, rawPacket);

        InetAddress localIpForSubnet = routeSelector.getLocalIpForSubnet(dst);

        Set<NodeConnector> physicalConnectors = nodeConnectorHandler.getPhysicalConnectors();

        for (NodeConnector physicalConnector : physicalConnectors) {
            String name = serviceHelper.getSwitchManager().getNodeConnectorProp(physicalConnector, "name").getStringValue();
            if(name.startsWith("eth")||name.startsWith("veth")) {
                byte[] nodeMAC = serviceHelper.getSwitchManager().getNodeMAC(physicalConnector.getNode());
                arpHelperWrapper.sendARPRequest(physicalConnector, nodeMAC, localIpForSubnet, dst);
            }
        }

    }


    @Override
    public RawPacket getAndRemoveAfterArp(InetAddress address) {

        RawPacket rawPacket = inetPacketCache.getEntry(address);

        inetPacketCache.remove(address);

        return rawPacket;
    }

    /**
     * This method iterates on all the waiting flows
     * and removes the expired flows.
     */
    @Scheduled(fixedRate = EXPIRED_FLOWS_DELETE_INTERVAL)
    private void deleteOldWaitingFlows() {
        Set<InetAddress> waitingFlowsKeys = waitingFlows.keys();
        Iterator<InetAddress> it = waitingFlowsKeys.iterator();
        while (it.hasNext()) {
            InetAddress key = it.next();
            waitingFlows.getEntry(key); // if the entry has expired, it will be removed
        }
    }

}
