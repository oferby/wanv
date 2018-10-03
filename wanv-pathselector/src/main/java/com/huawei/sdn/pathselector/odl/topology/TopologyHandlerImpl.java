package com.huawei.sdn.pathselector.odl.topology;

import com.huawei.sdn.pathselector.odl.ServiceHelper;
import org.opendaylight.controller.sal.core.Host;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.core.UpdateType;
import org.opendaylight.controller.sal.packet.address.DataLinkAddress;
import org.opendaylight.controller.sal.packet.address.EthernetAddress;
import org.opendaylight.controller.topologymanager.ITopologyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.InetAddress;

/**
 * Created by oWX212574 on 12/7/2014.
 */
@Controller
public class TopologyHandlerImpl implements TopologyHandler{

    @Autowired
    private ServiceHelper serviceHelper;

    @Override
    public void addHost(NodeConnector inConnector, byte[] macAddress, InetAddress srcIpAddress) {

        try {

            DataLinkAddress dataLinkAddress = new EthernetAddress(macAddress);

            Host host = new Host(dataLinkAddress, srcIpAddress);

            serviceHelper.getTopologyManager().updateHostLink(inConnector, host, UpdateType.CHANGED,null);

        } catch (Exception e){
            e.printStackTrace();
        }


    }

}
