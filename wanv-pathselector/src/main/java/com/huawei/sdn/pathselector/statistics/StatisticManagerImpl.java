package com.huawei.sdn.pathselector.statistics;

import com.huawei.sdn.commons.data.ConnectorStatistics;
import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.enums.ConnectorType;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import com.huawei.sdn.pathselector.odl.ServiceHelper;
import com.huawei.sdn.pathselector.odl.flow.FlowManager;
import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.opendaylight.controller.sal.reader.NodeConnectorStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 3/24/2015.
 */
@Component
public class StatisticManagerImpl implements StatisticManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticManagerImpl.class);

    @Autowired
    private RouteSelector routeSelector;

    @Autowired
    private ServiceHelper serviceHelper;

    @Autowired
    private FlowManager flowManager;

    @Autowired
    TaskScheduler taskScheduler;


    SimpleDateFormat dateFormat = new SimpleDateFormat();

    private Map<String, CircularFifoBuffer> bufferLinkStatisticsMap = new ConcurrentHashMap<>();
    private Map<String, Map<String, String>> lastStatistics = new ConcurrentHashMap<>();

    private static final String TIME_STAMP = "timeStamp";
    private static final String TX_BYTES = " TX";
    private static final String RX_BYTES = " RX";

    public StatisticManagerImpl() {

        dateFormat.applyPattern("kk:mm:ss");

    }

    @Override
    public List<PSConnector> getConnectorStatistics() {


        Set<PSConnector> allConnectors = routeSelector.getAllConnectors();
        List<PSConnector> connectorList = new ArrayList<>();
        connectorList.addAll(allConnectors);

        List<NodeConnectorStatistics> allConnectorStatistics = this.getAllConnectorStatistics();
        Map<String, NodeConnectorStatistics> connectorStatisticsMap = new HashMap<>();
        for (NodeConnectorStatistics connectorStatistic : allConnectorStatistics) {
            connectorStatisticsMap.put(connectorStatistic.getNodeConnector().getID().toString(), connectorStatistic);
        }

        Map<String, Integer> flowPerConnector = flowManager.getFlowPerConnector();

        for (Iterator<PSConnector> iterator = connectorList.iterator(); iterator.hasNext(); ) {

            PSConnector connector = iterator.next();

            if (connector.getType() == ConnectorType.LOCAL || !flowPerConnector.containsKey(connector.getId())) {
                iterator.remove();
                continue;
            }

            NodeConnectorStatistics nodeConnectorStatistics = connectorStatisticsMap.get(connector.getId());
            if (nodeConnectorStatistics == null)
                continue;

            ConnectorStatistics connectorStatistics = connector.getConnectorStatistics();
            connectorStatistics.setRxPackets(nodeConnectorStatistics.getReceivePacketCount());
            connectorStatistics.setTxPackets(nodeConnectorStatistics.getTransmitPacketCount());
            connectorStatistics.setRxBytes(nodeConnectorStatistics.getReceiveByteCount());
            connectorStatistics.setTxBytes(nodeConnectorStatistics.getTransmitByteCount());
            connectorStatistics.setRxDrops(nodeConnectorStatistics.getReceiveDropCount());
            connectorStatistics.setTxDrops(nodeConnectorStatistics.getTransmitDropCount());
            connectorStatistics.setRxErrors(nodeConnectorStatistics.getReceiveErrorCount());
            connectorStatistics.setTxErrors(nodeConnectorStatistics.getTransmitErrorCount());

            Integer numberOfFlows = flowPerConnector.get(connector.getId());
            if (numberOfFlows != null)
                connectorStatistics.setFlows(numberOfFlows);
            else
                connectorStatistics.setFlows(0);

        }

        return connectorList;
    }


    public List<NodeConnectorStatistics> getAllConnectorStatistics() {

        try {

            return serviceHelper.getStatisticsManager().getNodeConnectorStatistics(serviceHelper.getNode());

        } catch (Exception e) {
            return new ArrayList<>();
        }

    }

    @Override
    public List<Map<String, String>> getSiteTunnelStatistics(String siteId) {

        CircularFifoBuffer circularFifoBuffer = bufferLinkStatisticsMap.get(siteId);

        if (circularFifoBuffer == null) {
            return new ArrayList<>();
        }

        Iterator iterator = circularFifoBuffer.iterator();
        List<Map<String, String>> mapList = new ArrayList<>();

        while (iterator.hasNext()) {

            Map<String, String> next = (Map<String, String>) iterator.next();
            mapList.add(next);

        }

        return mapList;
    }

    @PostConstruct
    private void startScheduler() {

        taskScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                StatisticManagerImpl.this.scheduleStatistics();
            }
        }, new CronTrigger("0/10 * * * * ?"));

    }


    public void scheduleStatistics() {

        try {


            List<NodeConnectorStatistics> nodeConnectorStatisticsList = this.getAllConnectorStatistics();

            String timeStamp = dateFormat.format(new Date());

            Map<String, NodeConnectorStatistics> nodeConnectorStatisticsMap = new HashMap<>();

            for (NodeConnectorStatistics nodeConnectorStatistics : nodeConnectorStatisticsList) {

                String nodeConnectorId = nodeConnectorStatistics.getNodeConnector().getID().toString();

                nodeConnectorStatisticsMap.put(nodeConnectorId, nodeConnectorStatistics);

            }

//             add tunnel ids per site
            Map<String, Set<String>> connectorIdsPerTunnel = routeSelector.getConnectorIdPerTunnel();

//            add default gateways connectors to dummy site
            List<String> defaultRoutesIdList = routeSelector.getDefaultRoutesIdList();
            Set<String> defaultGateways = connectorIdsPerTunnel.get("defaultGateways");
            if(defaultGateways==null){
                defaultGateways = new HashSet<>();
                connectorIdsPerTunnel.put("defaultGateways",defaultGateways);
            }

            for (String gatewayConnectorId : defaultRoutesIdList) {
                defaultGateways.add(gatewayConnectorId);
            }


            for (String siteId : connectorIdsPerTunnel.keySet()) {

                if(connectorIdsPerTunnel.get(siteId).size()==0){
                    continue;
                }

                CircularFifoBuffer tunnelLinkRateBuffer = bufferLinkStatisticsMap.get(siteId);

                if (tunnelLinkRateBuffer == null) {
                    tunnelLinkRateBuffer = new CircularFifoBuffer(100);
                    bufferLinkStatisticsMap.put(siteId, tunnelLinkRateBuffer);

                }

                Map<String, String> lastSiteTunnelStatistics = lastStatistics.get(siteId);

                if (lastSiteTunnelStatistics == null) {
                    lastSiteTunnelStatistics = new HashMap<>();
                    lastStatistics.put(siteId, lastSiteTunnelStatistics);
                }

                Map<String, String> properties = new HashMap<>();

                properties.put("timeStamp", timeStamp);

                long currentRxKBytes;
                long currentTxKBytes;
                for (String tunnelId : connectorIdsPerTunnel.get(siteId)) {

                    NodeConnectorStatistics nodeConnectorStatistics = nodeConnectorStatisticsMap.get(tunnelId);

//                    in case the connector is default gateway, we could get here before we fix the connector id
                    if(nodeConnectorStatistics==null){
                        continue;
                    }

                    currentRxKBytes = nodeConnectorStatistics.getReceiveByteCount() / 1024;
                    currentTxKBytes = nodeConnectorStatistics.getTransmitByteCount() / 1024;

                    if (!lastSiteTunnelStatistics.containsKey("Link " + tunnelId + TX_BYTES)) {

                        properties.put("Link " + tunnelId + TX_BYTES, "0");
                        properties.put("Link " + tunnelId + RX_BYTES, "0");

                        lastSiteTunnelStatistics.put("Link " + tunnelId + TX_BYTES, String.valueOf(currentTxKBytes));
                        lastSiteTunnelStatistics.put("Link " + tunnelId + RX_BYTES, String.valueOf(currentRxKBytes));


                    } else {

                        long lastTxBytes = Long.parseLong(lastSiteTunnelStatistics.get("Link " + tunnelId + TX_BYTES));
                        long lastRxBytes = Long.parseLong(lastSiteTunnelStatistics.get("Link " + tunnelId + RX_BYTES));

                        long currentTxDelta = currentTxKBytes - lastTxBytes;
                        long currentRxDelta = currentRxKBytes - lastRxBytes;

                        properties.put("Link " + tunnelId + TX_BYTES, String.valueOf(currentTxDelta));
                        properties.put("Link " + tunnelId + RX_BYTES, String.valueOf(currentRxDelta));

                        lastSiteTunnelStatistics.put("Link " + tunnelId + TX_BYTES, String.valueOf(currentTxKBytes));
                        lastSiteTunnelStatistics.put("Link " + tunnelId + RX_BYTES, String.valueOf(currentRxKBytes));

                    }

                }

                tunnelLinkRateBuffer.add(properties);

            }

        } catch (RuntimeException e) {
            LOGGER.error("Got exception: " + e.getMessage());
        }

    }

}
