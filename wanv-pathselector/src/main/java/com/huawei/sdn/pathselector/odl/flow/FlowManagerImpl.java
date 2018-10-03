package com.huawei.sdn.pathselector.odl.flow;

import com.huawei.sdn.commons.cache.ExpirableCache;
import com.huawei.sdn.commons.data.MacAddress;
import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.enums.ConnectorType;
import com.huawei.sdn.commons.selector.csp.PathSelectorSolverImpl;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import com.huawei.sdn.commons.db.model.FlowEntity;
import com.huawei.sdn.pathselector.odl.L2.NodeConnectorHandler;
import com.huawei.sdn.pathselector.odl.ServiceHelper;
import com.huawei.sdn.pathselector.statistics.FlowStatisticsController;
import org.opendaylight.controller.sal.action.*;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.flowprogrammer.Flow;
import org.opendaylight.controller.sal.packet.Ethernet;
import org.opendaylight.controller.sal.packet.RawPacket;
import org.opendaylight.controller.sal.reader.FlowOnNode;
import org.opendaylight.controller.sal.reader.IReadService;
import org.opendaylight.controller.sal.utils.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by oWX212574 on 12/8/2014.
 */
@Component
public class FlowManagerImpl implements FlowManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowManagerImpl.class);

    @Autowired
    private ServiceHelper serviceHelper;

    @Autowired
    @Qualifier("arpCache")
    public ExpirableCache<String, MacAddress> arpCache;

    @Autowired
    @Qualifier("macNodeCache")
    public ExpirableCache<MacAddress, NodeConnector> macNodeCache;

    @Autowired
    private NodeConnectorHandler nodeConnectorHandler;

    @Autowired
    private RouteSelector routeSelector;

    @Autowired
    private PathSelectorSolverImpl pathSelectorSolver;

    @Autowired
    private FlowStatisticsController flowStatisticsController;

    @Autowired
    private TaskExecutor taskExecutor;

    @Override
    public void addFlow(PSFlow flow) {

        LOGGER.debug("Adding new flow id: " + flow.getId());

        switch (flow.getConnectorOut().getType()) {

            case GRE: {

                this.saveGreFlow(flow);
                break;
            }

            case REMOTE: {

                this.saveDefaultGatewayFlow(flow);
                break;
            }

            default: {
                LOGGER.error("callback for connector type " + flow.getConnectorOut().getType() + " not supported.");
            }
        }

    }


    @Override
    public void modifyFlow(PSFlow psFlow) {

        Flow oldFlow = (Flow) psFlow.getCtData();
        oldFlow.setId(0);

        Flow newFlow = this.cloneFlow(oldFlow);

        if (psFlow.getConnectorOut().getType().equals(ConnectorType.DROP)) {

            LOGGER.debug("Flow changed to DROP action");
            setFlowToDrop(oldFlow, newFlow);
            return;
        }

        List<Action> actions = newFlow.getActions();

        for (int i = 0; i < actions.size(); i++) {
            if (actions.get(i).getClass() == Enqueue.class || actions.get(i).getClass() == Drop.class) {

                if (psFlow.getConnectorOut().getType() == ConnectorType.LOCAL) {

                    NodeConnector port = ((Enqueue) actions.get(i)).getPort();
                    actions.set(i, new Enqueue(port, psFlow.getQueue().getId()));

                } else {

                    NodeConnector outputConnector = serviceHelper.getNodeConnector(new Short(psFlow.getConnectorOut().getId()));
                    actions.set(i, new Enqueue(outputConnector, psFlow.getQueue().getId()));

                }

                break;
            }
        }

        newFlow.setActions(actions);

        modifyFlow(oldFlow, newFlow);

    }


    private void setFlowToDrop(Flow oldFlow, Flow newFlow) {

        List<Action> actionList = new ArrayList<>();
        actionList.add(new Drop());
        newFlow.setActions(actionList);

        modifyFlow(oldFlow, newFlow);

    }

    private void modifyFlow(Flow oldFlow, Flow newFlow) {

        Node node = serviceHelper.getNode();

        LOGGER.debug("modifying psFlow in switch: " + oldFlow.toString());

        Status status = serviceHelper.getProgrammer().modifyFlowAsync(node, oldFlow, newFlow);

        LOGGER.debug("Modify status: " + status.toString());

    }

    private Flow cloneFlow(Flow oldFlow) {

        Flow newFlow = new Flow();
        newFlow.setId(oldFlow.getId());
        newFlow.setPriority(oldFlow.getPriority());
        newFlow.setIdleTimeout(oldFlow.getIdleTimeout());
        newFlow.setActions(oldFlow.getActions());
        newFlow.setMatch(oldFlow.getMatch());

        return newFlow;

    }

    @Override
    public void removeFlow(PSFlow flow) {

        IReadService readService = serviceHelper.getReadService();
        Node node = serviceHelper.getNode();

        List<FlowOnNode> flowOnNodes = readService.readAllFlows(node);

        for (FlowOnNode flowOnNode : flowOnNodes) {

            if (flowOnNode.getFlow().getId() == flow.getId()) {
                LOGGER.debug("removing flow from switch: " + flowOnNode.getFlow().toString());
                serviceHelper.getProgrammer().removeFlowAsync(node, flowOnNode.getFlow());
                break;
            }

        }

    }

    private Map<Long, Flow> getFlowMap() {

        IReadService readService = serviceHelper.getReadService();
        Node node = serviceHelper.getNode();
        Map<Long, Flow> flowMap = new HashMap<>();

        List<FlowOnNode> flowOnNodes = readService.readAllFlows(node);

        Flow flow;
        for (FlowOnNode flowOnNode : flowOnNodes) {
            flow = flowOnNode.getFlow();
            flowMap.put(flow.getId(), flow);
        }

        return flowMap;
    }

    private void saveDefaultGatewayFlow(final PSFlow<Flow, Ethernet> flow) {

        LOGGER.debug("Adding default gateway flow: " + flow.toString());

        PSConnector connectorOut = flow.getConnectorOut();

        MacAddress routerMacAddress = arpCache.getEntry(connectorOut.getNextHopIpAddress());

        NodeConnector nodeConnector = macNodeCache.getEntry(routerMacAddress);

        Flow defaultGatewayFlow = FlowFactory.createDefaultGatewayFlow(
                connectorOut.getLocalMacAddress(),
                routerMacAddress.getMac(),
                flow.getSrcIp(),
                flow.getDstIp(),
                flow.getNwProto(),
                flow.getTpSrc(),
                flow.getTpDst(),
                nodeConnector,
                flow.getQueue() == null ? 0 : flow.getQueue().getId(),
                flow.getId()
        );

        saveFlowToSwitch(defaultGatewayFlow);
        sendPacket(null, flow.getPacket(), defaultGatewayFlow);

        saveFlowToDb(flow);

    }

    private void saveGreFlow(PSFlow<Flow, Ethernet> flow) {

        LOGGER.debug("Adding GRE flow: " + flow.getCtData().toString());
        saveFlowToSwitch(flow.getCtData());

    }

    public void saveFlowToSwitch(Flow flow) {

        Status status = serviceHelper.getProgrammer().addFlowAsync(serviceHelper.getNode(), flow);

        LOGGER.debug("Save flow status:" + status.toString());

    }


    public void sendPacket(RawPacket rawPacket, Ethernet packet, Flow flow) {

        NodeConnector outConnector = null;

        List<Action> actions = flow.getActions();

        for (Action action : actions) {

            switch (action.getType()) {

                case HW_PATH: {
                    byte[] nodeMAC = serviceHelper.getNodeMacAddress();
                    packet.setDestinationMACAddress(nodeMAC);
                    continue;
                }

                case ENQUEUE: {
                    outConnector = (((Enqueue) action).getPort());
                    continue;
                }

                case SET_DL_DST: {
                    packet.setDestinationMACAddress(((SetDlDst) action).getDlAddress());
                    continue;
                }

                case SET_DL_SRC: {
                    packet.setSourceMACAddress(((SetDlSrc) action).getDlAddress());
                    continue;

                }

            }

        }

        RawPacket outPacket;
        try {
            outPacket = serviceHelper.getDataPacketService().encodeDataPacket(packet);

            if (outPacket == null) {
                throw new RuntimeException("out packet is NULL");
            }


        } catch (Exception e) {
            LOGGER.error("got error while trying to encode packet. error: " + e.getMessage());
            return;
        }

        if (rawPacket != null) {
            outPacket.setIncomingNodeConnector(rawPacket.getIncomingNodeConnector());
        }

        if (outConnector == null)
            outConnector = nodeConnectorHandler.getNodeConnector((short) 0);

        outPacket.setOutgoingNodeConnector(outConnector);

        LOGGER.debug("Transmitting first packet to connector " + outConnector.getID());
        serviceHelper.getDataPacketService().transmitDataPacket(outPacket);

    }

    public Map<String, Integer> getFlowPerConnectorFromSolution() {

        Set<PSFlow> flowSet = pathSelectorSolver.getAllKnownFlowsFromSolution();

        Map<String, Integer> flowPerConnector = new HashMap<>();

        Set<PSConnector> allConnectors = routeSelector.getAllConnectors();

        for (PSConnector connector : allConnectors) {
            flowPerConnector.put(connector.getId(), 0);
        }

        for (PSFlow psFlow : flowSet) {
            PSConnector connectorOut = psFlow.getConnectorOut();
            if (connectorOut != null) {
                Integer flows = flowPerConnector.get(connectorOut.getId());
                if (flows != null)
                    flowPerConnector.put(connectorOut.getId() + "", (flows + 1));
            }
        }

        return flowPerConnector;

    }

    public Map<String, Integer> getFlowPerConnector() {

        return this.getFlowPerConnector(null);

    }

    private Map<String, Integer> getFlowPerConnector(Set<String> idSet) {

        List<FlowOnNode> flowOnNodes = serviceHelper.getReadService().readAllFlows(serviceHelper.getNode());
        Map<String, Integer> flowPerConnector = new HashMap<>();

//        make sure we have 0 for connectors without flows
        Set<NodeConnector> physicalConnectors = serviceHelper.getPhysicalConnectors();
        for (NodeConnector physicalConnector : physicalConnectors) {
            if (idSet == null) {
                flowPerConnector.put(physicalConnector.getID().toString(), 0);
            } else {

                String id = physicalConnector.getID().toString();
                if (idSet.contains(id)) {
                    flowPerConnector.put(id, 0);
                }
            }

        }

        for (FlowOnNode flowOnNode : flowOnNodes) {

            List<Action> actions = flowOnNode.getFlow().getActions();
            for (Action action : actions) {

                String id = null;

                if (action instanceof Output) {
                    id = ((Output) action).getPort().getID().toString();
                } else if (action instanceof Enqueue) {
                    id = ((Enqueue) action).getPort().getID().toString();
                }

                if (id != null) {

                    Integer count = flowPerConnector.get(id);

                    if (idSet != null) {
                        if (idSet.contains(id)) {
                            flowPerConnector.put(id, count + 1);
                        }

                    } else {
                        flowPerConnector.put(id, count + 1);
                    }

                    break;
                }

            }

        }

        return flowPerConnector;

    }


    @Override
    public Map<String, Integer> getFlowPerTunnelConnector() {

        List<PSConnector> tunnelConnectorList = routeSelector.getTunnelConnectorList();
        Set<String> idSet = new HashSet<>();
        for (PSConnector defaultRoute : tunnelConnectorList) {
            idSet.add(defaultRoute.getId());
        }

        return this.getFlowPerConnector(idSet);
    }

    @Override
    public Map<String, Integer> getFlowPerGatewayConnector() {
        List<PSConnector> defaultRoutes = routeSelector.getDefaultRoutes();


        Set<String> idSet = new HashSet<>();
        for (PSConnector defaultRoute : defaultRoutes) {
            idSet.add(defaultRoute.getId());
        }

        return this.getFlowPerConnector(idSet);

    }


    private void saveFlowToDb(final PSFlow<Flow, Ethernet> psFlow) {

        if (psFlow.getTpDst() != null) {
            flowStatisticsController.save(FlowManagerImpl.this.getFlowEntity(psFlow));
        }

    }


    private FlowEntity getFlowEntity(PSFlow<Flow, Ethernet> psFlow) {

        return new FlowEntity(new Date(),
                psFlow.getSrcIp().getHostAddress(),
                psFlow.getDstIp().getHostAddress(),
                psFlow.getTpSrc().toString(),
                psFlow.getTpDst().toString()
        );

    }

}
