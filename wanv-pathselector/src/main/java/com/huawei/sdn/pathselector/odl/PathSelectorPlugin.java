package com.huawei.sdn.pathselector.odl;

import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerListener;
import org.opendaylight.controller.sal.packet.IListenDataPacket;

/**
 * The OpenDaylight First Frame Plug-in<br/>
 * It implements the OpenDayLight SAL interface {@link IListenDataPacket} to
 * intercept the Packet In and {@link IFlowProgrammerListener} to synchronize
 * the Path Selector Cache Flows.
 */
public class PathSelectorPlugin extends FlowSynchronizerPlugin implements IListenDataPacket, IFlowProgrammerListener {
}
