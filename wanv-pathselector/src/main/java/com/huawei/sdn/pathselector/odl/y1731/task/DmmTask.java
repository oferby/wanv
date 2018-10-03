package com.huawei.sdn.pathselector.odl.y1731.task;

import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.pathselector.odl.y1731.OamManager;
import com.huawei.sdn.pathselector.odl.y1731.OamManagerImpl;
import com.huawei.sdn.pathselector.odl.y1731.pdu.dmm.Dmm;
import com.huawei.sdn.pathselector.odl.y1731.pdu.dmm.LinkMeasurement;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.packet.Ethernet;
import org.opendaylight.controller.sal.packet.IDataPacketService;
import org.opendaylight.controller.sal.packet.RawPacket;

import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;

/**
 * Created by Ofer Ben-Yacov on 11/3/2014.
 */

public class DmmTask extends TimerTask {

    private ExecutorService executorService;

    private Map<NodeConnector, LinkMeasurement> nodeConnectorLinkMeasurementMap;

    private OamManagerImpl oamManager;

    private IDataPacketService dataPacketService;

    public DmmTask(ExecutorService executorService, Map<NodeConnector, LinkMeasurement> nodeConnectorLinkMeasurementMap, OamManagerImpl oamManager, IDataPacketService dataPacketService) {
        this.executorService = executorService;
        this.nodeConnectorLinkMeasurementMap = nodeConnectorLinkMeasurementMap;
        this.oamManager = oamManager;
        this.dataPacketService = dataPacketService;
    }

    @Override
    public void run() {

        for (final NodeConnector nodeConnector : nodeConnectorLinkMeasurementMap.keySet()) {

            executorService.execute(new Runnable() {
                @Override
                public void run() {

                    PSConnector greConnector = nodeConnectorLinkMeasurementMap.get(nodeConnector).getGreConnector();

                    Ethernet ethernetPacket = oamManager.getEthernetPacket(greConnector.getLocalMacAddress(), greConnector.getRemoteMacAddress());

                    ethernetPacket.setRawPayload(new Dmm().createPdu());

                    RawPacket rawPacket = dataPacketService.encodeDataPacket(ethernetPacket);
                    rawPacket.setOutgoingNodeConnector(nodeConnector);
                    dataPacketService.transmitDataPacket(rawPacket);

                }
            });

        }


    }
}
