package com.huawei.sdn.pathselector.jms;

import com.huawei.sdn.commons.data.CurrentLinkMetrics;
import com.huawei.sdn.commons.data.OFMetricsPerPort;
import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.selector.PathSelectorEngine;
import com.huawei.sdn.commons.data.PSConnector;

import java.util.List;
import java.util.Set;

/**
 * Created by root on 7/3/14.
 */

public class PathSelectorEngineMock implements PathSelectorEngine {

    private String currentLink;
    private boolean isConnected;

    public String getCurrentLink() {
        return currentLink;
    }

    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void enablePathSelector(boolean enable) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isPathSelectorEnabled() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PSConnector getConnectorForFlow(PSFlow flow, List<PSConnector> psConnectorList) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addNewFlow(PSFlow psFlow) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean flowExists(PSFlow psFlow) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void flowRemoved(PSFlow psFlowToRemove) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

//    @Override
//    public void flowRemoved(long flowId) {
//
//    }
//
//
//    @Override
//    public void flowRemoved(String connectorId, long flowId) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }

    @Override
    public void removeFlows(List<PSFlow> flowsToRemove) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void linkStatusChanged(String connectorId, boolean connected) {
        this.currentLink = connectorId;
        this.isConnected = connected;
    }

    @Override
    public void metricsReceived(List<CurrentLinkMetrics> metricsList) {

    }

    @Override
    public void metricsOVSReceived(List<OFMetricsPerPort> metricsPerPorts) {

    }

    @Override
    public Set<PSFlow> getAllKnownFlows() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getScore() {
        return 0;
    }

}
