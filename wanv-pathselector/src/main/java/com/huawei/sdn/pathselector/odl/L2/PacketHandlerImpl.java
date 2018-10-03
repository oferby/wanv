package com.huawei.sdn.pathselector.odl.L2;

import com.huawei.sdn.commons.cache.ExpirableCache;
import com.huawei.sdn.commons.config.ConfigurationLoader;
import com.huawei.sdn.commons.config.system.OsNetworkHelper;
import com.huawei.sdn.commons.config.topology.Queue;
import com.huawei.sdn.commons.data.MacAddress;
import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.enums.ConnectorType;
import com.huawei.sdn.commons.selector.PathSelectorEngine;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import com.huawei.sdn.commons.tools.NetUtils;
import com.huawei.sdn.pathselector.engine.FlowHelper;
import com.huawei.sdn.pathselector.odl.ArpHandler;
import com.huawei.sdn.pathselector.odl.ServiceHelper;
import com.huawei.sdn.pathselector.odl.flow.FlowFactory;
import com.huawei.sdn.pathselector.odl.flow.FlowManager;
import com.huawei.sdn.pathselector.odl.topology.TopologyHandler;
import com.huawei.sdn.pathselector.odl.y1731.OamManager;
import com.huawei.sdn.pathselector.statistics.FlowStatisticsController;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.flowprogrammer.Flow;
import org.opendaylight.controller.sal.packet.*;
import org.opendaylight.controller.sal.utils.IPProtocols;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/1/14
 * Time: 1:52 PM
 * To change this template use File | Settings | File Templates.
 */
@Component("l2PacketHandler")
public class PacketHandlerImpl implements PacketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketHandlerImpl.class);
    private static final int ARP_FLOW_INTERVAL = 60000;
    public final static short ETHERNET_TYPE_Y1731 = (short) (0xffff & 0x8902);
    private final String BROADCAST_ADDRESS = "255.255.255.255";

    @Autowired
    private OamManager oamManager;

    @Autowired
    @Qualifier("arpCache")
    public ExpirableCache<String, MacAddress> arpCache;
    @Autowired
    @Qualifier("macNodeCache")
    public ExpirableCache<MacAddress, NodeConnector> macNodeCache;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private RouteSelector routeSelector;
    @Autowired
    private PathSelectorEngine pathSelectorEngine;
    @Autowired
    private ArpHandler arpHandler;

    @Autowired
    private ServiceHelper serviceHelper;

    @Autowired
    private TopologyHandler topologyHandler;

    @Autowired
    private FlowManager flowManager;

    @Autowired
    private FlowStatisticsController flowStatisticsController;

    @Autowired
    private ConfigurationLoader configurationLoader;

    @Autowired
    private OsNetworkHelper osNetworkHelper;

    private long lastArpFlowCreation = 0;
    private Random r = new Random();
    private AtomicLong transactionIdGenerator = new AtomicLong();
    private Queue defaultQueue;

    @Override
    public boolean handlePacket(RawPacket rawPacket, Ethernet packet) {

        if (packet.getEtherType() == ETHERNET_TYPE_Y1731) {
            oamManager.handleIncoming(rawPacket, packet);
            return true;
        }

        long transactionId = transactionIdGenerator.getAndIncrement();

        LOGGER.debug("Got incoming packet on port " + rawPacket.getIncomingNodeConnector().getID() + " . Transaction id:" + transactionId);

        this.addToMacCache(rawPacket, packet);

        if (checkAndHandleArp(rawPacket, packet))
            return true;


//        NodeConnector outConnector = macNodeCache.getEntry(new MacAddress(packet.getDestinationMACAddress()));
//
//        if (outConnector != null) {
//            LOGGER.debug("sending packet to LAN");
//            sendToLocalNetwork(rawPacket, outConnector);
//            return true;
//        }

        Packet payload = packet.getPayload();

        if (!(payload instanceof IPv4))
            return false;

        LOGGER.debug("packet is IPv4");

        IPv4 iPv4 = (IPv4) payload;

//        if (sameIpSubnet(rawPacket, iPv4)) {
//            LOGGER.debug("IP packet is for the same subnet");
//            return true;
//        }

        if (iPv4.getProtocol() == IPProtocols.GRE.byteValue()) {
            LOGGER.debug("got incoming GRE packet");
            this.handleGreTraffic(iPv4, rawPacket, packet);
            return true;

        }

        String srcIpAddress = NetUtils.getInetAddress(iPv4.getSourceAddress()).getHostAddress();


        InetAddress dstAddress = NetUtils.getInetAddress(iPv4.getDestinationAddress());
        String dstAddressHostAddress = dstAddress.getHostAddress();
        PSConnector outConnector;

        if (isDHCP(dstAddressHostAddress, iPv4)) {
            return true;
        }

        if (this.isReferenceUrlMonitorPacket(srcIpAddress, dstAddressHostAddress)) {
            LOGGER.debug("Got monitor packet. src: " + srcIpAddress + ", dst: " + dstAddressHostAddress);
            outConnector = routeSelector.getConnector(srcIpAddress, dstAddressHostAddress, false);
            handleDefaultGatewayPath(rawPacket, packet, iPv4, dstAddress, outConnector, false);
            return true;

        }

        outConnector = routeSelector.getConnector(srcIpAddress, dstAddressHostAddress, true);

        if (outConnector == null) {
            LOGGER.debug("There are no active links");
            return true;
        }

        LOGGER.debug("Got from route selector connector ID: " + outConnector.getId());

        if (outConnector.getType().equals(ConnectorType.LOCAL)) {
            handleConnectedIpPacket(rawPacket, iPv4, dstAddress, outConnector);
            return true;
        }

        if (outConnector.getType().equals(ConnectorType.GRE)) {
            handleGrePath(rawPacket, iPv4, dstAddress, outConnector);
            return true;
        }

        if (outConnector.getType().equals(ConnectorType.REMOTE)) {
            handleDefaultGatewayPath(rawPacket, packet, iPv4, dstAddress, outConnector, true);
            return true;
        }

        LOGGER.info("packet was not handled by IPv4");

        return false;
    }

    private boolean isDHCP(String dstAddress, IPv4 iPv4) {

        if (dstAddress.equals(BROADCAST_ADDRESS)) {

            if (iPv4.getPayload() instanceof UDP) {

                final short destinationPort = ((UDP) iPv4.getPayload()).getDestinationPort();

                if (destinationPort == 67 || destinationPort == 68) {

                    LOGGER.debug("Got DHCP request");

                    taskExecutor.execute(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                Flow dhcpFlow = FlowFactory.createDHCPFlow(destinationPort);

                                flowManager.saveFlowToSwitch(dhcpFlow);

                            } catch (RuntimeException e) {

                                LOGGER.error("Got Exception: ", e);

                            }

                        }
                    });

                    return true;

                }

            }

        }

        return false;

    }

    private boolean isReferenceUrlMonitorPacket(String src, String dst) {

        if (routeSelector.isSelfIpAddress(src)) {

            String referenceUrl = configurationLoader.getReferenceUrl();

            URI uri;
            try {

                uri = new URI(referenceUrl);

                if (uri.getHost().equals(dst)) {
                    return true;
                }

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }

        return false;
    }


    private void handleGreTraffic(IPv4 iPv4, RawPacket rawPacket, Ethernet ethernet) {

        InetAddress dstIp = NetUtils.getInetAddress(iPv4.getDestinationAddress());

        InetAddress srcIp = NetUtils.getInetAddress(iPv4.getSourceAddress());

        Flow greTrafficFlow;

        long flowId = FlowFactory.flowIdGenerator.incrementAndGet();

        if (routeSelector.isSelfIpAddress(dstIp.getHostAddress())) {

            greTrafficFlow = FlowFactory.createGreIncomingFlow(flowId, srcIp, dstIp);

        } else {

            PSConnector connector = routeSelector.getConnector(srcIp.getHostAddress(), dstIp.getHostAddress(), false);

            String nextHopIpAddress = connector.getNextHopIpAddress();

            MacAddress macAddress = arpCache.getEntry(nextHopIpAddress);

            if (macAddress == null) {
                LOGGER.warn("MAC address not learned yet. Sending ARP request");
                InetAddress nextHopInetAddress;
                try {
                    nextHopInetAddress = InetAddress.getByName(nextHopIpAddress);
                } catch (UnknownHostException e) {
                    LOGGER.error("Got exception on getting iNetAddress", e);
                    return;
                }

                arpHandler.sendARPRequest(connector.getLocalMacAddress(), srcIp, nextHopInetAddress);
                return;
            }

            NodeConnector nodeConnector = macNodeCache.getEntry(macAddress);

            greTrafficFlow = FlowFactory.createGreOutgoingFlow(flowId, srcIp, dstIp, connector.getLocalMacAddress(), macAddress.getMac(), nodeConnector);

        }

        flowManager.saveFlowToSwitch(greTrafficFlow);

        flowManager.sendPacket(rawPacket, ethernet, greTrafficFlow);

    }

    public void addToMacCache(final RawPacket rawPacket, final Ethernet packet) {

        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {

                try {

                    NodeConnector incomingNodeConnector = rawPacket.getIncomingNodeConnector();
                    String name = serviceHelper.getSwitchManager().getNodeConnectorProp(incomingNodeConnector, "name").getStringValue();
                    if (name.contains("eth") || name.contains("veth")) {
                        addToMacCache(packet, incomingNodeConnector);
//            createMacBasedLocalFlow(packet, incomingNodeConnector);
                    }

                } catch (RuntimeException e) {

                    LOGGER.error("Got Exception: ", e);

                }

            }
        });

    }

    private void handleDefaultGatewayPath(final RawPacket rawPacket, final Ethernet packet, final IPv4 iPv4, final InetAddress dstAddress, final PSConnector outConnector, final boolean useSrcPort) {

        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {

                LOGGER.debug("handling packet to default gateway");

                boolean selfIP = false;
                PSConnector incomingConnector;

                String srcIpAddress = NetUtils.getInetAddress(iPv4.getSourceAddress()).getHostAddress();
                if (routeSelector.isSelfIpAddress(srcIpAddress)) {
                    selfIP = true;
                } else {

                    incomingConnector = routeSelector.getConnector(rawPacket.getIncomingNodeConnector().getID().toString());

                    if (incomingConnector != null && !Arrays.equals(packet.getDestinationMACAddress(), incomingConnector.getLocalMacAddress()) && incomingConnector.getType() != ConnectorType.GRE) {
                        LOGGER.debug("Destination MAC is not of the switch. Ignoring packet ");
                        return;
                    }

                }

                if (!PacketHandlerImpl.this.getGatewaysWithMacAddress(outConnector)) {
                    LOGGER.error("there are no MAC entries for any GW");
                    return;
                }

                String nextHopIpAddress = outConnector.getNextHopIpAddress();

                MacAddress macAddress = arpCache.getEntry(nextHopIpAddress);

                NodeConnector nodeConnector = macNodeCache.getEntry(macAddress);

                Flow defaultGatewayFlow = FlowFactory.createDefaultGatewayFlow(iPv4,
                        outConnector.getLocalMacAddress(),
                        macAddress, nodeConnector, useSrcPort);

                if (selfIP) {
                    LOGGER.debug("Packet sent from switch IP");
                    flowManager.saveFlowToSwitch(defaultGatewayFlow);
                    return;
                }

                PSFlow<Flow, Ethernet> psFlow = FlowHelper.getPsFlow(defaultGatewayFlow, dstAddress, outConnector.getType(), defaultQueue, packet, outConnector.getGroupId());
                psFlow.setConnectorOut(outConnector);
                flowManager.addFlow(psFlow);

                packet.setDestinationMACAddress(macAddress.getMac());

                PacketHandlerImpl.this.sendToLocalNetwork(packet, nodeConnector);

//                checkAndSaveFlow(defaultGatewayFlow, psFlow, false);

            }
        });

    }

    public void sendArpToDefaultGateways() {

        List<PSConnector> defaultRoutes = routeSelector.getDefaultRoutes();

        for (PSConnector defaultRoute : defaultRoutes) {
            this.getGatewaysWithMacAddress(defaultRoute);

        }

    }

    private boolean getGatewaysWithMacAddress(final PSConnector psConnector) {

        MacAddress gwMac = arpCache.getEntry(psConnector.getNextHopIpAddress());

        if (gwMac != null)
            return true;

        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {

                try {

                    InetAddress dstIP = InetAddress.getByName(psConnector.getNextHopIpAddress());
                    InetAddress localIp = routeSelector.getLocalIpForSubnet(dstIP);

                    byte[] nodeMAC = osNetworkHelper.getMacAddress(localIp.getHostAddress());

                    arpHandler.sendARPRequest(nodeMAC, localIp, dstIP);

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }


            }
        });

        return false;

    }

    private void handleGrePath(final RawPacket rawPacket, final IPv4 iPv4, final InetAddress dstAddress, final PSConnector outConnector) {

        LOGGER.debug("handling packet for remote LAN");

        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {

                try {

                    LOGGER.debug("handling packet for remote LAN using connector id: " + outConnector.getId());

                    NodeConnector nodeConnector = serviceHelper.getNodeConnector(Short.parseShort(outConnector.getId()));

//        Send first packet to random GRE link
                    rawPacket.setOutgoingNodeConnector(nodeConnector);
                    serviceHelper.getDataPacketService().transmitDataPacket(rawPacket);

//        Write the flow to the switch and path selector solver
                    Flow greFlow = FlowFactory.createGreFlow(FlowFactory.flowIdGenerator.incrementAndGet(), iPv4, dstAddress, nodeConnector);

                    PSFlow<Flow, Ethernet> psFlow = FlowHelper.getPsFlow(greFlow, dstAddress, outConnector.getType(), defaultQueue, null, outConnector.getGroupId());
                    psFlow.setConnectorOut(outConnector);
                    checkAndSaveFlow(greFlow, psFlow, true);

                } catch (RuntimeException e) {

                    LOGGER.error("Got Exception: ", e);

                }

            }
        });

    }

    private void checkAndSaveFlow(Flow flow, PSFlow psFlow, boolean saveToSwitch) {

        if (!pathSelectorEngine.isPathSelectorEnabled() || saveToSwitch) {
            flowManager.saveFlowToSwitch(flow);
        }

        if (pathSelectorEngine.isPathSelectorEnabled()) {

            if (pathSelectorEngine.flowExists(psFlow)) {
                LOGGER.debug("Flow already in path selector engine");
                return;
            }

            pathSelectorEngine.addNewFlow(psFlow);

        }

        flowStatisticsController.save(psFlow);

    }

    private void handleConnectedIpPacket(final RawPacket rawPacket, final IPv4 iPv4, final InetAddress dstIpAddress, final PSConnector psConnector) {

        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {

                Flow flow = null;

                try {

                    LOGGER.debug("handling connected IP");

                    InetAddress srcIp = NetUtils.getInetAddress(iPv4.getSourceAddress());

                    boolean sendToLogicEngine = false;
                    NodeConnector outConnector = null;

                    // handle switch traffic
                    if (routeSelector.getLocalIpAddresses().contains(dstIpAddress)) {

                        flow = FlowFactory.createConnectedIpFlow(
                                srcIp,
                                dstIpAddress,
                                null,
                                null,
                                null);

                    } else {

                        MacAddress macAddress = arpCache.getEntry(dstIpAddress.getHostAddress());

                        if (macAddress == null) {
                            arpHandler.sendARPRequest(dstIpAddress);
                            return;
                        }

                        outConnector = macNodeCache.getEntry(macAddress);

                        flow = FlowFactory.createConnectedIpFlow(srcIp, dstIpAddress,
                                osNetworkHelper.getMacAddress(psConnector.getLocalIPAddress()),
                                macAddress.getMac(), outConnector, iPv4);

                        sendToLogicEngine = true;

                    }

                    flowManager.saveFlowToSwitch(flow);
                    flowManager.sendPacket(rawPacket, (Ethernet) iPv4.getParent(), flow);

                    if (sendToLogicEngine) {

                        PSFlow<Flow, Ethernet> psFlow = FlowHelper.getPsFlow(flow, dstIpAddress, ConnectorType.LOCAL, defaultQueue, (Ethernet) iPv4.getParent(), "");
                        PSConnector connector = routeSelector.getConnector(outConnector.getID().toString());
                        psFlow.setConnectorOut(connector);

                        pathSelectorEngine.addNewFlow(psFlow);
                    }

                } catch (RuntimeException e) {

                    LOGGER.error("Got Exception for flow: " + (flow == null ? "null" : flow.toString()), e);

                }


            }
        });


    }

    private boolean checkAndHandleArp(final RawPacket rawPacket, final Ethernet packet) {

        if (!(packet.getPayload() instanceof ARP)) {
            return false;
        }

        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {

                try {

                    ARP arpPacket = (ARP) packet.getPayload();

                    InetAddress srcAddress;
                    NodeConnector incomingNodeConnector = rawPacket.getIncomingNodeConnector();
                    try {

                        srcAddress = InetAddress.getByAddress(arpPacket.getSenderProtocolAddress());
                        MacAddress macAddress = new MacAddress(arpPacket.getSenderHardwareAddress());
                        String hostAddress = srcAddress.getHostAddress();
                        LOGGER.debug("Adding entry to ARP cache: " + hostAddress + " :: " + macAddress);
                        arpCache.addEntry(hostAddress, macAddress);

                        if (!routeSelector.isSelfIpAddress(srcAddress.getHostAddress())) {
                            topologyHandler.addHost(rawPacket.getIncomingNodeConnector(), arpPacket.getSenderHardwareAddress(), srcAddress);
                        }

//            Set<NodeConnector> physicalNodeConnectors = switchManager.getPhysicalNodeConnectors(incomingNodeConnector.getNode());
//            if (physicalNodeConnectors.contains(incomingNodeConnector)) {
//                LOGGER.debug("adding MAC to mac-node cache");
//                macNodeCache.addEntry(macAddress, incomingNodeConnector);
//
//            }

                        if (routeSelector.isDefaultGateway(hostAddress)) {
                            LOGGER.debug("this is default router ip");
                            routeSelector.setDefaultRoutePortId(hostAddress, (Short) incomingNodeConnector.getID());

                        }

                    } catch (UnknownHostException e) {
                        LOGGER.error(e.getMessage());
                        throw new RuntimeException(e);
                    }

                    if (arpPacket.getOpCode() == ARP.REQUEST) {

                        LOGGER.debug("packet is ARP Request");

                        long timeInMillis = Calendar.getInstance().getTimeInMillis();

                        if (timeInMillis > lastArpFlowCreation + ARP_FLOW_INTERVAL) {

                            lastArpFlowCreation = timeInMillis;

                            Flow arpFlow = FlowFactory.createArpFlow();

                            LOGGER.debug("Adding ARP flow");
                            flowManager.saveFlowToSwitch(arpFlow);

                        }

                    } else {
                        LOGGER.debug("packet is ARP Reply");

                    }


                } catch (RuntimeException e) {

                    LOGGER.error("Got Exception: ", e);

                }

            }
        });

        return true;

    }

    private void sendToLocalNetwork(Ethernet packet, NodeConnector nodeConnector) {

        RawPacket rawPacket = serviceHelper.getDataPacketService().encodeDataPacket(packet);
        rawPacket.setOutgoingNodeConnector(nodeConnector);

        serviceHelper.getDataPacketService().transmitDataPacket(rawPacket);
    }

    private void addToMacCache(Ethernet inPkt, NodeConnector nodeConnector) {

        LOGGER.debug("adding MAC to macNodeCache: " + getByteArrayString(inPkt.getSourceMACAddress()) + ", port id: " + nodeConnector.getID());
        macNodeCache.addEntry(new MacAddress(inPkt.getSourceMACAddress()), nodeConnector);

    }

    private String getByteArrayString(byte[] bytes) {

        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X ", b));
            result.append(":");
        }

        return result.toString();

    }

    @Override
    public void enablePathSelector(boolean enable) {
        pathSelectorEngine.enablePathSelector(enable);
    }

    @Override
    public boolean isPathSelectorEnabled() {
        return pathSelectorEngine.isPathSelectorEnabled();
    }

    @PostConstruct
    private void setup() {

        defaultQueue = configurationLoader.getDefaultQueue();

    }

}
