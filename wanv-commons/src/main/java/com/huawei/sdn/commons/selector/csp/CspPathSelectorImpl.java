package com.huawei.sdn.commons.selector.csp;

import com.huawei.sdn.commons.data.CurrentLinkMetrics;
import com.huawei.sdn.commons.data.OFMetricsPerPort;
import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.selector.PathSelectorEngine;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import com.huawei.sdn.commons.data.ConnectorStatistics;
import com.huawei.sdn.commons.data.PSConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * Created by oWX212574 on 6/23/2014.
 */
@Controller("pathSelectorEngine")
public class CspPathSelectorImpl implements PathSelectorEngine {

    private static Logger logger = LoggerFactory.getLogger(CspPathSelectorImpl.class);
    @Autowired
    private PathSelectorSolver pathSelectorSolver;
    //    @Autowired
//    private ExecutorService executorService;
    private Random r = new Random();
    @Autowired
    private RouteSelector routeSelector;

    private boolean pathSelectorEnabled = true;

    @Override
    public void metricsReceived(List<CurrentLinkMetrics> metricsList) {

        Map<String, ConnectorStatistics> connectorStatisticsFromSolution = pathSelectorSolver.getConnectorStatisticsFromSolution();
        Map<String, ConnectorStatistics> newConnectorStatisticsMap = new HashMap<>();

        for (CurrentLinkMetrics currentLinkMetrics : metricsList) {
            ConnectorStatistics connectorStatistics = this.getConnectorStatistics(currentLinkMetrics);
            if (connectorStatistics != null && !connectorStatistics.equals(connectorStatisticsFromSolution.get(connectorStatistics.getLinkId()))) {
                newConnectorStatisticsMap.put(connectorStatistics.getLinkId(), connectorStatistics);

            }
        }

        if (!newConnectorStatisticsMap.isEmpty()) {
            logger.trace("Sending statistics to solver");
            pathSelectorSolver.connectorMetrics(newConnectorStatisticsMap);
        }

    }

    private ConnectorStatistics getConnectorStatistics(CurrentLinkMetrics linkMetrics) {

        if (linkMetrics.getCurrentStats() == null)
            return null;

        ConnectorStatistics statistics = new ConnectorStatistics();

        statistics.setLinkId(linkMetrics.getLinkId());
        statistics.setFrameDelay(linkMetrics.getFrameDelay());
        statistics.setFrameDelayVariation(linkMetrics.getFrameDelayVariation());
        statistics.setPacketLossRate(linkMetrics.getPacketLossRate());

        return statistics;
    }

    @Override
    public void metricsOVSReceived(List<OFMetricsPerPort> metricsPerPorts) {

    }

    @Override
    public Set<PSFlow> getAllKnownFlows() {

        return pathSelectorSolver.getAllKnownFlows();

    }

    @Override
    public int getScore() {
        return pathSelectorSolver.getScore();
    }

    @Override
    public void enablePathSelector(boolean enable) {
        pathSelectorEnabled = enable;
        pathSelectorSolver.enableSolver(enable);
    }

    @Override
    public boolean isPathSelectorEnabled() {
        return pathSelectorEnabled;
    }

    @Override
    public PSConnector getConnectorForFlow(final PSFlow flow, final List<PSConnector> psConnectorList) {

        if (psConnectorList.size() == 0)
            return null;


        List<PSConnector> validConnectors = new ArrayList<>();

        for (PSConnector psConnector : psConnectorList) {
            if (psConnector.isActive())
                validConnectors.add(psConnector);
        }

        if (validConnectors.size() == 0)
            return null;

        PSConnector outConnector;
        if (validConnectors.size() == 1)
            outConnector = validConnectors.get(0);
        else
            outConnector = validConnectors.get(r.nextInt(validConnectors.size()));

        flow.setType(outConnector.getType());
        flow.setConnectorOut(outConnector);

        logger.debug("Adding flow to solver");
        pathSelectorSolver.addFlow(flow);

        return outConnector;

    }

    @Override
    public void addNewFlow(PSFlow psFlow) {
        logger.debug("Adding flow to solver");
        pathSelectorSolver.addFlow(psFlow);
    }

    @Override
    public boolean flowExists(PSFlow psFlow) {
        return pathSelectorSolver.flowExists(psFlow);
    }

    @Override
    public void flowRemoved(PSFlow psFlowToRemove) {
        pathSelectorSolver.removeFlow(psFlowToRemove);
    }

    @Override
    public void removeFlows(List<PSFlow> flowsToRemove) {

        for (PSFlow psFlow : flowsToRemove) {
            pathSelectorSolver.removeFlow(psFlow);
        }


    }

    @Override
    public void linkStatusChanged(String connectorId, boolean connected) {

        pathSelectorSolver.portStatusChanged(connectorId, connected);

        routeSelector.getConnector(connectorId).setActive(connected);


    }


}
