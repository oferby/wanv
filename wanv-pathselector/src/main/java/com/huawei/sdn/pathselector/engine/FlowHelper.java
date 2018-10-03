package com.huawei.sdn.pathselector.engine;

import com.huawei.sdn.commons.config.topology.Queue;
import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.enums.ConnectorType;
import com.huawei.sdn.pathselector.odl.flow.FlowFactory;
import com.huawei.sdn.pathselector.tools.BeanFactory;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.flowprogrammer.Flow;
import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerService;
import org.opendaylight.controller.sal.match.MatchField;
import org.opendaylight.controller.sal.packet.Ethernet;
import org.opendaylight.controller.sal.packet.IPv4;
import org.opendaylight.controller.sal.utils.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

/**
 * utility class to add/modify/remove flow in opendaylight
 */
public final class FlowHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowHelper.class);

    /**
     * utility class with only static methods
     */
    private FlowHelper() {
    }

    /**
     * retrieve the ODL flow programmer in the bean factory
     *
     * @return the ODL flow programmer
     */
    private static IFlowProgrammerService getIFlowProgrammerService() {
        return (IFlowProgrammerService) BeanFactory.getInstance().getBean(IFlowProgrammerService.class);
    }

    /**
     * Remove an existing flow
     *
     * @param f    the existing flow
     * @param node the node that have this flow
     * @return true if succeeded else false
     * @throws Exception
     */
    public static boolean removeFlow(Flow f, Node node) {
        if (f == null) {
            return true;
        }
        final Status status = getIFlowProgrammerService().removeFlow(node, f);
        LOGGER.info("-------- Removing a flow: " + f);
        if (!status.isSuccess()) {
            LOGGER.warn("Failed to remove the flow: {}. The failure is: {}", f, status.getDescription());
            return false;
        }
        return true;
    }


    public static PSFlow<Flow, Ethernet> getPsFlow(Flow flow, InetAddress dstAddress, ConnectorType connectorType, Queue queue, Ethernet packet, String groupId) {

        PSFlow<Flow, Ethernet> psFlow = getPsFlow(flow, dstAddress);
        psFlow.setType(connectorType);
        psFlow.setQueue(queue);
        psFlow.setPacket(packet);
        psFlow.setGroupId(groupId);

        return psFlow;


    }



    public static PSFlow<Flow, Ethernet> getPsFlow(Flow flow, InetAddress dstAddress) {

        PSFlow<Flow, Ethernet> psFlow = new PSFlow<>();
        psFlow.setId(FlowFactory.flowIdGenerator.incrementAndGet());
        psFlow.setCtData(flow);
        psFlow.setDstIp(dstAddress);


        for (MatchField matchField : flow.getMatch().getMatchFields()) {

            switch (matchField.getType()) {

                case DL_TYPE: {
                    psFlow.setDlType((Short) matchField.getValue());
                    break;
                }
//                case DL_SRC: {
//                    break;
//                }
//                case DL_DST: {
//
//                    break;
//                }
                case NW_TOS: {
                    psFlow.setNwTOS((Byte) matchField.getValue());
                    break;
                }

                case NW_PROTO: {
                    psFlow.setNwProto((Byte) matchField.getValue());
                    break;
                }
                case NW_SRC: {
                    psFlow.setSrcIp((InetAddress) matchField.getValue());
                    break;
                }
                case NW_DST: {
                    psFlow.setDstIp((InetAddress) matchField.getValue());
                    break;
                }
                case TP_SRC: {
                    psFlow.setTpSrc((Short) matchField.getValue());
                    break;
                }
                case TP_DST: {
                    psFlow.setTpDst((Short) matchField.getValue());
                    break;
                }


            }

        }


//        temp

//        PSPacketIn packetIn = new PSPacketIn(null);
//        packetIn.setNwTOS(new Byte("0"));
//
//
//        psFlow.setPSPacket(packetIn);

        return psFlow;

    }
}
