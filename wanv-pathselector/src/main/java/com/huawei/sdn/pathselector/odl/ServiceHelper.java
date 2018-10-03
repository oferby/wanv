package com.huawei.sdn.pathselector.odl;

import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerService;
import org.opendaylight.controller.sal.packet.IDataPacketService;
import org.opendaylight.controller.sal.reader.IReadService;
import org.opendaylight.controller.statisticsmanager.IStatisticsManager;
import org.opendaylight.controller.switchmanager.ISwitchManager;
import org.opendaylight.controller.topologymanager.ITopologyManager;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/6/14
 * Time: 11:29 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ServiceHelper {
    ISwitchManager getSwitchManager();

    IFlowProgrammerService getProgrammer();

    IDataPacketService getDataPacketService();

    IReadService getReadService();

    IStatisticsManager getStatisticsManager();

    ITopologyManager getTopologyManager();

    Node getNode();

    NodeConnector getNodeConnector(String id);

    NodeConnector getNodeConnector(Short id);

    Set<NodeConnector> getPhysicalConnectors();

    byte[] getNodeMacAddress();

}
