package com.huawei.sdn.pathselector.arp;

import com.huawei.sdn.commons.tools.NetUtils;
import com.huawei.sdn.pathselector.engine.ArpHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opendaylight.controller.sal.packet.Ethernet;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.net.InetAddress;

/**
 * Created by root on 7/1/14.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ArpHandlerContext.class})
public class ArpHandlerTest {

//    @Autowired
//    private ArpHandler arpHandler;
//    @Autowired
//    private ExpirableCache<InetAddress, MacAddress> ipToMac;
//    @Autowired
//    private ExpirableCache<InetAddress, PSFlow> waitingFlows;


    @Test
    public void createArp(){

        InetAddress src = NetUtils.parseInetAddress("1.1.1.1");
        InetAddress dst = NetUtils.parseInetAddress("1.1.1.2");
        byte[] sMAc = {0x01,0x02,0x03,0x04,0x05,0x06};
        Ethernet arpPacket = ArpHelper.getArpPacket(sMAc, src, dst);

        Assert.notNull(arpPacket);


    }


//    @Test
    public void testArpManager() throws Exception {
        InetAddress keyAddress = NetUtils.parseInetAddress("1.1.1.1");
/*
            PSConnector connIn = new PSConnector("in1");
            PSConnector connOut = new PSConnector("out1");
            PSFlow psFlow = new PSFlow(new PSPacketIn(null), connIn, connOut);
            psFlow.setDstIp(keyAddress);
            Node node = new Node(NodeConnector.NodeConnectorIDType.CONTROLLER, "1");
            arpHandler.sendMacRequest(psFlow, node);

            PSFlow waitingFlow =  waitingFlows.getEntry(keyAddress);
            assertNotNull(waitingFlow);
            assertTrue(waitingFlow.getDstIp().equals(keyAddress));
*/


/*
            arpHandler.handleMacResponse(InetAddress.getByName("1.1.1.1"), new byte[]{1,1,1,1});
            MacAddress mac = ipToMac.getEntry(keyAddress);
            assertNotNull(mac);
*/
    }

}
