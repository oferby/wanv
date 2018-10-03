package com.huawei.sdn.pathselector.odl;

import com.huawei.sdn.commons.context.WanApplicationContextProvider;
import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.selector.flow.FlowStatusHandler;
import org.opendaylight.controller.sal.action.Action;
import org.opendaylight.controller.sal.action.Output;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.flowprogrammer.Flow;
import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerListener;
import org.opendaylight.controller.sal.packet.Ethernet;
import org.opendaylight.controller.sal.packet.IListenDataPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The OpenDaylight First Frame Plug-in<br/>
 * It implements the OpenDayLight SAL interface {@link IListenDataPacket} to
 * intercept the Packet In and {@link IFlowProgrammerListener} to synchronize
 * the Path Selector Cache Flows.
 */
public class FlowSynchronizerPlugin extends PackectInPlugin implements IFlowProgrammerListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlowSynchronizerPlugin.class);

    private FlowStatusHandler flowStatusHandler;

    /*
     * (non-Javadoc)
     *
     * @see org.opendaylight.controller.sal.flowprogrammer.
     * IPluginOutFlowProgrammerService
     * #flowRemoved(org.opendaylight.controller.sal.core.Node,
     * org.opendaylight.controller.sal.flowprogrammer.Flow)
     */
    @Override
    public void flowRemoved(Node node, Flow flow) {
        LOGGER.debug("Flow removed: " + flow.toString());

        if(flowStatusHandler==null){

            if(WanApplicationContextProvider.isReady()){
                flowStatusHandler = WanApplicationContextProvider.getBean(FlowStatusHandler.class);
                assert flowStatusHandler!=null;
            } else {
                LOGGER.error("Spring context not ready.");
                return;
            }

        }

        PSFlow<Flow, Ethernet>psFlowToRemove = new PSFlow<>();
        psFlowToRemove.setCtData(flow);

        flowStatusHandler.flowRemoved(psFlowToRemove);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.opendaylight.controller.sal.flowprogrammer.
     * IPluginOutFlowProgrammerService
     * #flowErrorReported(org.opendaylight.controller.sal.core.Node, long,
     * java.lang.Object)
     */
    @Override
    public void flowErrorReported(Node node, long rid, Object err) {
        LOGGER.error("error " + rid + ": " + err.toString());
    }
}
