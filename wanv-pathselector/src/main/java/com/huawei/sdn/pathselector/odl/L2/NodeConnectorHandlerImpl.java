package com.huawei.sdn.pathselector.odl.L2;

import com.huawei.sdn.pathselector.odl.ServiceHelper;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.switchmanager.ISwitchManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/1/14
 * Time: 4:41 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller("nodeConnectorHandler")
public class NodeConnectorHandlerImpl implements NodeConnectorHandler {

    @Autowired
    private ServiceHelper serviceHelper;

    private Map<Short, NodeConnector> nodeConnectorMap = new ConcurrentHashMap<>();
    private Set<NodeConnector> physicalNodeConnectorSet = new HashSet<>();

    @Override
    public Set<NodeConnector> getPhysicalConnectors() {

        return physicalNodeConnectorSet;
    }

    @Override
    public boolean isPhysical(NodeConnector nodeConnector) {
        return nodeConnectorMap.containsKey(nodeConnector.getID());
    }

    public void init() {

        Set<Node> nodes = serviceHelper.getSwitchManager().getNodes();

        if (nodes.isEmpty())
            throw new NotImplementedException();


        for (Node node : nodes) {

            for (NodeConnector nodeConnector : serviceHelper.getSwitchManager().getNodeConnectors(node)) {
                nodeConnectorMap.put((Short) nodeConnector.getID(), nodeConnector);
                if(serviceHelper.getSwitchManager().getNodeConnectorProp(nodeConnector, "bandwidth")!=null)
                    physicalNodeConnectorSet.add(nodeConnector);

            }
        }

    }

    @Override
    public NodeConnector getNodeConnector(Short id) {
        return nodeConnectorMap.get(id);
    }
}
