package com.huawei.sdn.commons.selector.csp;

import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.data.ConnectorStatistics;

import java.util.Map;
import java.util.Set;

/**
 * Created by Ofer Ben-Yacov on 6/23/2014.
 */
public interface PathSelectorSolver {

    int getScore();

    PSFlow getCurrentFlow(long id);

    void addFlow(PSFlow flow);

    boolean flowExists(PSFlow psFlow);

    void removeFlow(PSFlow flow);

    void portStatusChanged(String connectorId, boolean connected);

    Set<PSFlow> getAllKnownFlows();

    Set<PSFlow> getAllKnownFlowsFromSolution();

    void addPSConnector(PSConnector psConnector);

    void modifyPSConnector(PSConnector psConnector);

    void remotePSConnector(PSConnector psConnector);

    void connectorMetrics(Map<String,ConnectorStatistics> connectorStatisticsMap);

    Map<String,ConnectorStatistics>getConnectorStatisticsFromSolution();

    void enableSolver(boolean enable);

    void restart();

}
