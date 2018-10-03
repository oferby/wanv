package com.huawei.sdn.pathselector.web.constraint;

import com.huawei.sdn.pathselector.odl.ServiceHelper;
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
 * Created by Ofer Ben-Yacov (oWX212574) on 6/17/2015.
 */
public class ServiceHelperMock implements ServiceHelper {
    @Override
    public ISwitchManager getSwitchManager() {
        return null;
    }

    @Override
    public IFlowProgrammerService getProgrammer() {
        return null;
    }

    @Override
    public IDataPacketService getDataPacketService() {
        return null;
    }

    @Override
    public IReadService getReadService() {
        return null;
    }

    @Override
    public IStatisticsManager getStatisticsManager() {
        return null;
    }

    @Override
    public ITopologyManager getTopologyManager() {
        return null;
    }

    @Override
    public Node getNode() {
        return null;
    }

    @Override
    public NodeConnector getNodeConnector(String id) {
        return null;
    }

    @Override
    public NodeConnector getNodeConnector(Short id) {
        return null;
    }

    @Override
    public Set<NodeConnector> getPhysicalConnectors() {
        return null;
    }

    @Override
    public byte[] getNodeMacAddress() {
        return new byte[0];
    }
}
