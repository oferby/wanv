package com.huawei.sdn.pathselector.web.constraint;

import com.huawei.sdn.commons.data.ConnectorStatistics;
import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.selector.csp.PathSelectorSolver;

import java.util.Map;
import java.util.Set;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 6/17/2015.
 */
public class PathSelectorSolverMock implements PathSelectorSolver {
    @Override
    public int getScore() {
        return 0;
    }

    @Override
    public PSFlow getCurrentFlow(long id) {
        return null;
    }

    @Override
    public void addFlow(PSFlow flow) {

    }

    @Override
    public boolean flowExists(PSFlow psFlow) {
        return false;
    }

    @Override
    public void removeFlow(PSFlow flow) {

    }

    @Override
    public void portStatusChanged(String connectorId, boolean connected) {

    }

    @Override
    public Set<PSFlow> getAllKnownFlows() {
        return null;
    }

    @Override
    public Set<PSFlow> getAllKnownFlowsFromSolution() {
        return null;
    }

    @Override
    public void addPSConnector(PSConnector psConnector) {

    }

    @Override
    public void modifyPSConnector(PSConnector psConnector) {

    }

    @Override
    public void remotePSConnector(PSConnector psConnector) {

    }

    @Override
    public void connectorMetrics(Map<String, ConnectorStatistics> connectorStatisticsMap) {

    }

    @Override
    public Map<String, ConnectorStatistics> getConnectorStatisticsFromSolution() {
        return null;
    }

    @Override
    public void enableSolver(boolean enable) {

    }

    @Override
    public void restart() {

    }
}
