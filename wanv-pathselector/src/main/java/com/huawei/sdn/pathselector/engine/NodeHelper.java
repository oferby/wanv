package com.huawei.sdn.pathselector.engine;

import com.huawei.sdn.pathselector.tools.BeanFactory;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.switchmanager.ISwitchManager;

/**
 * Created by root on 6/30/14.
 */
public class NodeHelper {

    /**
     * Retrieve the Node Connector OpenDayLight Object of an OpenDayLight Node
     *
     * @param node
     *            an OpenDayLight Node
     * @param id
     *            The Node Connector id
     * @return the Node Connector OpenDayLight Object if exist else null
     */
    public static NodeConnector getNodeConnector(Node node, String id) {
        ISwitchManager switchManager = (ISwitchManager) BeanFactory.getInstance().getBean(ISwitchManager.class);
        for (final NodeConnector nodeConnector : switchManager.getNodeConnectors(node)) {
            if (nodeConnector.getNodeConnectorIDString().equals(id)) {
                return nodeConnector;
            }
        }
        return null;
    }
}
