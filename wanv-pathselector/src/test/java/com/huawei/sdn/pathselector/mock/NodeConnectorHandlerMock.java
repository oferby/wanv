package com.huawei.sdn.pathselector.mock;

import com.huawei.sdn.pathselector.odl.L2.NodeConnectorHandler;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.switchmanager.ISwitchManager;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/1/14
 * Time: 5:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class NodeConnectorHandlerMock implements NodeConnectorHandler{
    @Override
    public Set<NodeConnector> getPhysicalConnectors() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isPhysical(NodeConnector nodeConnector) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void init() {

    }

    @Override
    public NodeConnector getNodeConnector(Short id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
