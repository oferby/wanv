package com.huawei.sdn.pathselector.odl.y1731;

import com.huawei.sdn.commons.data.CurrentLinkMetrics;
import com.huawei.sdn.commons.data.PSConnector;
import org.opendaylight.controller.sal.packet.Ethernet;
import org.opendaylight.controller.sal.packet.RawPacket;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Ofer Ben-Yacov on 10/1/2014.
 */
public interface OamManager {
    void startTimer();
    void handleIncoming(RawPacket rawPacket, Ethernet packet);

}
