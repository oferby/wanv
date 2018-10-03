package com.huawei.sdn.pathselector.odl.y1731.task;

import com.huawei.sdn.commons.data.CurrentLinkMetrics;
import com.huawei.sdn.commons.selector.PathSelectorEngine;
import com.huawei.sdn.pathselector.odl.y1731.pdu.ccm.CcmHandler;
import com.huawei.sdn.pathselector.odl.y1731.pdu.dmm.LinkMeasurement;
import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * Created by Ofer Ben-Yacov on 11/3/2014.
 */
public class StatisticsTask extends TimerTask {

    private final Logger LOGGER = LoggerFactory.getLogger(StatisticsTask.class);

    private ExecutorService executorService;

    private Map<NodeConnector, LinkMeasurement> nodeConnectorLinkMeasurementMap;

    private PathSelectorEngine pathSelectorEngine;

    private Map<NodeConnector, CcmHandler> nodeConnectorEthernetMap;

    public StatisticsTask(ExecutorService executorService, Map<NodeConnector, LinkMeasurement> nodeConnectorLinkMeasurementMap, PathSelectorEngine pathSelectorEngine, Map<NodeConnector, CcmHandler> nodeConnectorEthernetMap) {
        this.executorService = executorService;
        this.nodeConnectorLinkMeasurementMap = nodeConnectorLinkMeasurementMap;
        this.pathSelectorEngine = pathSelectorEngine;
        this.nodeConnectorEthernetMap = nodeConnectorEthernetMap;
    }

    @Override
    public void run() {

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                //            could be that one one the measurements are not active
                Set<NodeConnector> nodeConnectors = nodeConnectorEthernetMap.keySet();
                if (nodeConnectors.isEmpty()) {
                    nodeConnectors = nodeConnectorLinkMeasurementMap.keySet();
                }

                List<CurrentLinkMetrics> currentLinkMetricsList = new ArrayList<>();
                CurrentLinkMetrics currentLinkMetrics;
                for (NodeConnector nodeConnector : nodeConnectors) {

                    currentLinkMetrics = new CurrentLinkMetrics();
                    currentLinkMetrics.setLinkId(nodeConnector.getID().toString());

                    CcmHandler ccmHandler = nodeConnectorEthernetMap.get(nodeConnector);
                    if (ccmHandler != null) {
                        currentLinkMetrics.setConnected(ccmHandler.getCcmStatus().isConnected());
                        currentLinkMetrics.setPacketLossRate(ccmHandler.getCcmStatus().getAndResetFrameLossRate(ccmHandler.getCcm().getPeriod()));

                    }

                    LinkMeasurement linkMeasurement = nodeConnectorLinkMeasurementMap.get(nodeConnector);
                    if (linkMeasurement != null) {
                        currentLinkMetrics.setFrameDelay((int) linkMeasurement.getFrameDelayMean());
                        currentLinkMetrics.setFrameDelayVariation((int) linkMeasurement.getFrameDelayStandardDeviation());
                    }

                    currentLinkMetricsList.add(currentLinkMetrics);
                }

                pathSelectorEngine.metricsReceived(currentLinkMetricsList);

            }
        });

    }

}
