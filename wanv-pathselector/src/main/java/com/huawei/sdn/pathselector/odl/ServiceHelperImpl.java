package com.huawei.sdn.pathselector.odl;

import com.huawei.sdn.commons.config.topology.TopologyServiceHelper;
import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerService;
import org.opendaylight.controller.sal.packet.IDataPacketService;
import org.opendaylight.controller.sal.reader.IReadService;
import org.opendaylight.controller.statisticsmanager.IStatisticsManager;
import org.opendaylight.controller.switchmanager.ISwitchManager;
import org.opendaylight.controller.topologymanager.ITopologyManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.NodeConnector;

import java.util.Collection;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/6/14
 * Time: 11:26 AM
 * To change this template use File | Settings | File Templates.
 */
@Component("serviceHelper")
public class ServiceHelperImpl implements ServiceHelper, TopologyServiceHelper {

    private ISwitchManager switchManager;
    private IFlowProgrammerService programmer;
    private IDataPacketService dataPacketService;
    private IReadService readService;
    private IStatisticsManager statisticsManager;
    private ITopologyManager topologyManager;

    private Node node;

    @Override
    public ISwitchManager getSwitchManager() {
        return switchManager;
    }

    @Override
    public IFlowProgrammerService getProgrammer() {
        return programmer;
    }

    @Override
    public IDataPacketService getDataPacketService() {
        return dataPacketService;
    }

    public IReadService getReadService() {
        return readService;
    }

    public void setReadService(IReadService readService) {
        this.readService = readService;
    }

    public void setSwitchManager(ISwitchManager switchManager) {
        this.switchManager = switchManager;
    }

    public void setProgrammer(IFlowProgrammerService programmer) {
        this.programmer = programmer;
    }

    public void setDataPacketService(IDataPacketService dataPacketService) {
        this.dataPacketService = dataPacketService;
    }

    public IStatisticsManager getStatisticsManager() {
        return statisticsManager;
    }

    @Override
    public ITopologyManager getTopologyManager() {
        return topologyManager;
    }

    public void setTopologyManager(ITopologyManager topologyManager) {
        this.topologyManager = topologyManager;
    }

    public void setStatisticsManager(IStatisticsManager statisticsManager) {
        this.statisticsManager = statisticsManager;
    }

    public Node getNode(){

        if(node!=null)
            return node;

        node = switchManager.getNodes().iterator().next();

        return node;
    }

    @Override
    public NodeConnector getNodeConnector(String id) {

        return this.getNodeConnector(new Short(id));

    }

    @Cacheable("switch")
    public NodeConnector getNodeConnector(Short id){

        Set<NodeConnector> nodeConnectors = switchManager.getNodeConnectors(getNode());

        for (NodeConnector nodeConnector : nodeConnectors) {
            if(nodeConnector.getID().equals(id))
                return nodeConnector;
        }

        return null;

    }

    @Cacheable("switch")
    public Set<NodeConnector> getPhysicalConnectors(){

        return switchManager.getPhysicalNodeConnectors(getNode());

    }

    @Cacheable("switch")
    public byte[] getNodeMacAddress(){
        return switchManager.getNodeMAC(getNode());
    }


    @Override
    public String getNodeId() {
        return this.getNode().getNodeIDString();
    }
}
