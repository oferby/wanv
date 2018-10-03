package com.huawei.sdn.pathselector.odl.L2;

import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.switchmanager.ISwitchManager;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/1/14
 * Time: 4:40 PM
 * To change this template use File | Settings | File Templates.
 */
public interface NodeConnectorHandler {

    Set<NodeConnector> getPhysicalConnectors();

    boolean isPhysical(NodeConnector nodeConnector);

    void init();

    NodeConnector getNodeConnector(Short id);


}
