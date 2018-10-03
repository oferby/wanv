package com.huawei.sdn.pathselector.odl.y1731.task;

import com.huawei.sdn.commons.selector.PathSelectorEngine;
import com.huawei.sdn.pathselector.odl.y1731.pdu.ccm.CcmHandler;
import com.huawei.sdn.pathselector.odl.y1731.pdu.dmm.LinkMeasurement;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.packet.IDataPacketService;
import org.opendaylight.controller.sal.packet.RawPacket;

import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;

/**
 * Created by Ofer Ben-Yacov on 11/3/2014.
 */
public class CcmTask extends TimerTask {

    private ExecutorService executorService;

    private Map<NodeConnector, CcmHandler> nodeConnectorEthernetMap;

    private IDataPacketService dataPacketService;

    public CcmTask(ExecutorService executorService, Map<NodeConnector, CcmHandler> nodeConnectorEthernetMap, IDataPacketService dataPacketService) {
        this.executorService = executorService;
        this.nodeConnectorEthernetMap = nodeConnectorEthernetMap;
        this.dataPacketService = dataPacketService;
    }

    @Override
    public void run() {

        for (final NodeConnector nodeConnector : nodeConnectorEthernetMap.keySet()) {

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    CcmHandler ccmAdapter = nodeConnectorEthernetMap.get(nodeConnector);
                    long txFc1 = ccmAdapter.getCcmStatus().increaseTxFc1();
                    ccmAdapter.getCcm().setTxFcf(txFc1);
                    ccmAdapter.resetCcmInEthernet();

//                        LOGGER.debug(ccmAdapter.getCcm().toString());

                    RawPacket rawPacket = dataPacketService.encodeDataPacket(ccmAdapter.getEthernet());
                    rawPacket.setOutgoingNodeConnector(nodeConnector);
                    dataPacketService.transmitDataPacket(rawPacket);
                }
            });

        }

    }

}
