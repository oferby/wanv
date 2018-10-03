package com.huawei.sdn.pathselector.odl.y1731;

import com.huawei.sdn.commons.cache.ExpirableCache;
import com.huawei.sdn.commons.config.ConfigurationLoader;
import com.huawei.sdn.commons.config.topology.OamManagerConfig;
import com.huawei.sdn.commons.config.topology.Operation;
import com.huawei.sdn.commons.data.MacAddress;
import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.selector.PathSelectorEngine;
import com.huawei.sdn.commons.selector.flow.FlowStatusHandler;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import com.huawei.sdn.pathselector.odl.L2.PacketHandler;
import com.huawei.sdn.pathselector.odl.ServiceHelper;
import com.huawei.sdn.pathselector.odl.y1731.pdu.Period;
import com.huawei.sdn.pathselector.odl.y1731.pdu.ccm.Ccm;
import com.huawei.sdn.pathselector.odl.y1731.pdu.ccm.CcmHandler;
import com.huawei.sdn.pathselector.odl.y1731.pdu.ccm.ConnectivityLossListener;
import com.huawei.sdn.pathselector.odl.y1731.pdu.dmm.Dmr;
import com.huawei.sdn.pathselector.odl.y1731.pdu.dmm.LinkMeasurement;
import com.huawei.sdn.pathselector.odl.y1731.task.*;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.packet.Ethernet;
import org.opendaylight.controller.sal.packet.RawPacket;
import org.opendaylight.controller.sal.reader.NodeConnectorStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Ofer Ben-Yacov on 9/23/2014.
 */
@Controller
public class OamManagerImpl implements OamManager, OamManagerConfig, ConnectivityLossListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(OamManagerImpl.class);

    public final static short ETHERNET_TYPE_Y1731 = (short) (0xffff & 0x8902);

    @Autowired
    private ServiceHelper serviceHelper;

    @Autowired
    private ConfigurationLoader configurationLoader;

    @Autowired
    private FlowStatusHandler flowStatusHandler;

    @Autowired
    private RouteSelector routeSelector;

    @Autowired
    private PathSelectorEngine pathSelectorEngine;

    @Autowired
    private PacketHandler packetHandler;

    @Autowired
    @Qualifier("arpCache")
    public ExpirableCache<String, MacAddress> arpCache;

    @Autowired
    @Qualifier("macNodeCache")
    public ExpirableCache<MacAddress, NodeConnector> macNodeCache;

    private ExecutorService executorService = new ThreadPoolExecutor(6, 12, 2, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20));
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(3);

    private Map<NodeConnector, CcmHandler> nodeConnectorEthernetMap  = new ConcurrentHashMap<>();
    private Map<NodeConnector, LinkMeasurement> nodeConnectorLinkMeasurementMap  = new ConcurrentHashMap<>();

    private Timer ccmTimer;

    private Timer dmmTimer;

    private Timer statisticTimer;

    private Period heartBitPeriod;
    private Operation heartBitOperation;

    private Period delayPeriod;
    private Operation delayOperation;

    public void startTimer() {

        if (routeSelector.getTunnelConnectorList().size() > 0 & statisticTimer == null) {
            startTunnelMonitoring();
        }

        this.startSingleMode();

    }


    private void startTunnelMonitoring() {

        LOGGER.debug("Starting tunnel monitoring");

        heartBitOperation = configurationLoader.getHeartBitOperation();

        if (heartBitOperation.isEnabled()) {
            LOGGER.debug("Starting hear bit operation");
            heartBitPeriod = Period.values()[heartBitOperation.getInterval().getPeriod()];
            startHeartBit();
        }

        delayOperation = configurationLoader.getDelayOperation();
        if (delayOperation.isEnabled()) {
            LOGGER.debug("Starting delay operation");
            delayPeriod = Period.values()[delayOperation.getInterval().getPeriod()];
            startDelayOperation();
        }

        if (delayOperation.isEnabled() || heartBitOperation.isEnabled()) {
            LOGGER.debug("Starting statistics timer");
            statisticTimer = new Timer("Statistics-Timer");
            statisticTimer.schedule(new StatisticsTask(executorService, nodeConnectorLinkMeasurementMap, pathSelectorEngine, nodeConnectorEthernetMap), 0, 1000);

        }


    }


    @Override
    public void handleIncoming(final RawPacket rawPacket, final Ethernet packet) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                byte[] rawPayload = packet.getRawPayload();
                NodeConnector incomingNodeConnector = rawPacket.getIncomingNodeConnector();

                if (rawPayload[1] == 1) {
//                    This is CCM frame

                    OamManagerImpl.this.handleIncomingCcm(incomingNodeConnector, rawPayload);

                } else if (rawPayload[1] == 47) {
//                  This is DMM frame
                    OamManagerImpl.this.handleIncomingDmm(incomingNodeConnector, packet, rawPayload);

                } else if (rawPayload[1] == 46) {
//                    This is DMR frame
                    OamManagerImpl.this.handleIncomingDmr(incomingNodeConnector, rawPayload);

                } else {

                    LOGGER.error("Un-known op-code: " + rawPayload[1]);

                }

            }
        });

    }

    private void handleIncomingCcm(NodeConnector nodeConnector, byte[] payload) {

        CcmHandler ccmHandler = nodeConnectorEthernetMap.get(nodeConnector);

        if(ccmHandler==null) {
            LOGGER.warn("got ccm message but there is no matching connector." );
            return;
        }

        ccmHandler.resetLoseOfConnectivityTimer();
        long rxFc1 = ccmHandler.getCcmStatus().increaseRxFc1();
        ccmHandler.getCcm().setRxFcb(rxFc1);
        ccmHandler.setLastMessageReceived();

        ByteBuffer bf = ByteBuffer.allocate(4).put(Arrays.copyOfRange(payload, 58, 62));
        bf.position(0);
        int txFcf = bf.getInt();

        ccmHandler.getCcm().setTxFcb(txFcf);

        bf = ByteBuffer.allocate(4).put(Arrays.copyOfRange(payload, 62, 66));
        bf.position(0);
        int rxFcb = bf.getInt();

        bf = ByteBuffer.allocate(4).put(Arrays.copyOfRange(payload, 66, 70));
        bf.position(0);
        int txFcb = bf.getInt();

        ccmHandler.getCcmStatus().calcFrameLoss(txFcb, rxFcb, txFcf);
        LOGGER.trace("Updating CcmHandler. " + ccmHandler.toString());
        if (!ccmHandler.getCcmStatus().isConnected()) {
            LOGGER.debug("Connector back to active state. ID:" + nodeConnector.getID().toString());
            ccmHandler.getCcmStatus().setConnected(true);
            flowStatusHandler.linkStatusChanged(nodeConnector.getID().toString(), true);
        }

    }


    /*
    * Create DMR frame, copy time values from DMM and send it back to the DMM sender.
    * */
    private void handleIncomingDmm(NodeConnector incomingNodeConnector, final Ethernet packet, byte[] payload) {

        Dmr dmr = new Dmr();
        byte[] dmrPdu = dmr.getPduFromDmm(payload);

        Ethernet ethernetPacket = this.getEthernetPacket(packet.getDestinationMACAddress(), packet.getSourceMACAddress());

        ethernetPacket.setRawPayload(dmrPdu);

        RawPacket rawPacket = serviceHelper.getDataPacketService().encodeDataPacket(ethernetPacket);
        rawPacket.setOutgoingNodeConnector(incomingNodeConnector);
        serviceHelper.getDataPacketService().transmitDataPacket(rawPacket);

    }

    private void handleIncomingDmr(NodeConnector incomingNodeConnector, byte[] payload) {

        long currentTimeMillis = System.currentTimeMillis();

        ByteBuffer bf = ByteBuffer.allocate(8);
        bf.put(Arrays.copyOfRange(payload, 4, 12));
        bf.position(0);
        long txTimeStampF = bf.getLong();

        LinkMeasurement linkMeasurement = nodeConnectorLinkMeasurementMap.get(incomingNodeConnector);

        linkMeasurement.addFrameDelay((currentTimeMillis - txTimeStampF) / 2);

    }


    /*
    * We create CCM for each node + tunnel and start sender timer according to the configured period
    * */
    private void startHeartBit() {

        LOGGER.debug("Starting heart bit operation");

        Collection<PSConnector> greConnectors = routeSelector.getTunnelConnectorList();

        for (PSConnector greConnector : greConnectors) {
            this.addGreConnectorHeartBit(greConnector);
        }

        LOGGER.debug("Starting CCM-Timer");

        ccmTimer = new Timer("CCM-Timer");

        ccmTimer.schedule(new CcmTask(executorService, nodeConnectorEthernetMap, serviceHelper.getDataPacketService()), 0, heartBitPeriod.getDelay());

    }

    public void greConnectorAdded(PSConnector greConnector) {

        LOGGER.debug("Adding GRE connector. " + greConnector.toString());

        if (statisticTimer == null) {
            startTunnelMonitoring();
            return;
        }

        if (heartBitOperation.isEnabled()) {
            addGreConnectorHeartBit(greConnector);
        }

        if (delayOperation.isEnabled()) {
            addGreConnectorDelayTest(greConnector);
        }


    }


    private void addGreConnectorHeartBit(PSConnector greConnector) {

        NodeConnector nodeConnector = serviceHelper.getNodeConnector(Short.valueOf(greConnector.getId()));

        if (nodeConnector == null) {
            LOGGER.error("could not find Node Connector id: " + greConnector.getId());
            return;
        }

        Ethernet ethernetPacket = this.getEthernetPacket(greConnector.getLocalMacAddress(), greConnector.getRemoteMacAddress());
        CcmHandler ccmHandler = new CcmHandler(nodeConnector.getID().toString(), new Ccm(heartBitPeriod, (short) 0, ""), ethernetPacket, this);
        LOGGER.debug("Adding CcmHandler. " + ccmHandler.toString());
        nodeConnectorEthernetMap.put(nodeConnector, ccmHandler);


    }


    private void startDelayOperation() {

        LOGGER.debug("Starting delay operation");

        Collection<PSConnector> greConnectors = routeSelector.getTunnelConnectorList();


        for (PSConnector greConnector : greConnectors) {
            this.addGreConnectorDelayTest(greConnector);
        }

        dmmTimer = new Timer("DMM-Timer");
        dmmTimer.schedule(new DmmTask(executorService, nodeConnectorLinkMeasurementMap, this, serviceHelper.getDataPacketService()),
                0, delayPeriod.getDelay());

    }


    private void addGreConnectorDelayTest(PSConnector greConnector) {

        LOGGER.debug("Adding GRE connector to delay test. " + greConnector.toString());

        NodeConnector nodeConnector = serviceHelper.getNodeConnector(Short.valueOf(greConnector.getId()));

        nodeConnectorLinkMeasurementMap.put(nodeConnector, new LinkMeasurement(greConnector, delayOperation.getSampleSize()));

    }


    public Ethernet getEthernetPacket(byte[] localMac, byte[] remoteMac) {

        Ethernet packet = new Ethernet();

        packet.setSourceMACAddress(localMac);
        packet.setDestinationMACAddress(remoteMac);
        packet.setEtherType(ETHERNET_TYPE_Y1731);

        return packet;
    }

    @Override
    public void connectivityLost(String connectorId) {
        LOGGER.debug("Connectivity lost to connector id: " + connectorId);
        flowStatusHandler.linkStatusChanged(connectorId, false);
    }


    private void startSingleMode() {

        LOGGER.debug("Starting single ended mode");

        Map<NodeConnector, TrafficRateStatistics> nodeConnectorDescriptiveStatisticsMap = new HashMap<>();

        List<NodeConnectorStatistics> nodeConnectorStatistics = serviceHelper.getStatisticsManager().getNodeConnectorStatistics(serviceHelper.getNode());

        for (NodeConnectorStatistics nodeConnectorStatistic : nodeConnectorStatistics) {
            nodeConnectorDescriptiveStatisticsMap.put(nodeConnectorStatistic.getNodeConnector(), new TrafficRateStatistics(nodeConnectorStatistic.getNodeConnector().getID().toString()));
        }

        packetHandler.sendArpToDefaultGateways();

        scheduledThreadPoolExecutor.schedule(new IngressTrafficCheckTask(flowStatusHandler, routeSelector, serviceHelper,
                nodeConnectorDescriptiveStatisticsMap, configurationLoader.getReferenceUrl(), configurationLoader.getTimeout(), scheduledThreadPoolExecutor), 10, TimeUnit.SECONDS);

    }


}
