package com.huawei.sdn.pathselector.odl.flow;

import com.huawei.sdn.commons.selector.flow.FlowProgrammer;
import org.opendaylight.controller.sal.flowprogrammer.Flow;
import org.opendaylight.controller.sal.packet.Ethernet;
import org.opendaylight.controller.sal.packet.RawPacket;

import java.util.Map;

/**
 * Created by oWX212574 on 12/8/2014.
 */
public interface FlowManager extends FlowProgrammer {

    void saveFlowToSwitch(Flow flow);

    void sendPacket(RawPacket rawPacket, Ethernet packet, Flow flow);

    Map<String, Integer> getFlowPerConnectorFromSolution();

    Map<String, Integer> getFlowPerConnector();

    Map<String, Integer> getFlowPerTunnelConnector();

    Map<String, Integer> getFlowPerGatewayConnector();


}
