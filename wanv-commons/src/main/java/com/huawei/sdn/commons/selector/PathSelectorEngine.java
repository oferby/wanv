package com.huawei.sdn.commons.selector;

import com.huawei.sdn.commons.data.CurrentLinkMetrics;
import com.huawei.sdn.commons.data.OFMetricsPerPort;
import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.data.PSConnector;

import java.util.List;
import java.util.Set;

/**
 * Created by Ofer Ben-Yacov on 6/25/2014.
 */
public interface PathSelectorEngine {

    void enablePathSelector(boolean enable);

    boolean isPathSelectorEnabled();

    PSConnector getConnectorForFlow(PSFlow flow, List<PSConnector> psConnectorList);

    void addNewFlow(PSFlow psFlow);

    boolean flowExists(PSFlow psFlow);

    void flowRemoved(PSFlow psFlowToRemove);

    void removeFlows(List<PSFlow> flowsToRemove);

    void linkStatusChanged(String connectorId, boolean connected);

    void metricsReceived(List<CurrentLinkMetrics> metricsList);

    void metricsOVSReceived(List<OFMetricsPerPort> metricsPerPorts);

    Set<PSFlow> getAllKnownFlows();

    int getScore();

}
