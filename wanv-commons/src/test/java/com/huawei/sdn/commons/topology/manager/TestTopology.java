package com.huawei.sdn.commons.topology.manager;

import com.huawei.sdn.commons.config.topology.*;
import com.huawei.sdn.commons.enums.OperationConfig;
import com.huawei.sdn.commons.enums.TimeInterval;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

/**
 * Created by Ofer Ben-Yacov on 10/1/2014.
 */
public class TestTopology {

    public static void main(String[] args) throws Exception {
        final Topology topology = new Topology();
        final Operation op1 = new Operation();
        op1.setEnabled(true);
        op1.setInterval(TimeInterval.INTERVAL_1_S);
        op1.setOperationConfig(OperationConfig.FRAME_DELAY);
        final Operation op2 = new Operation();
        op2.setEnabled(true);
        op2.setInterval(TimeInterval.INTERVAL_1_S);
        op2.setOperationConfig(OperationConfig.HEARTBEAT);
        topology.setOperations(new Operation[] { op1, op2 });

        Site site1 = new Site();
        site1.setId("site1");
        IpAddress ipAddress1 = new IpAddress();
        ipAddress1.setId(1);
        ipAddress1.setIp("10.1.50.2/30");
        IpAddress ipAddress2 = new IpAddress();
        ipAddress1.setId(2);
        ipAddress1.setIp("10.1.60.2/30");
        site1.setIpAddresses(new IpAddress[] {ipAddress1, ipAddress2});
        Route route11 = new Route();
        route11.setDestination("0.0.0.0/0");
        route11.setGateway("10.1.50.1");
        route11.setMaxRate(2);
        Route route12 = new Route();
        route12.setDestination("0.0.0.0/0");
        route12.setGateway("10.1.60.1");
        route12.setMaxRate(2);
        site1.setRoutes(new Route[]{route11, route12});

        Tunnel tunnel11 = new Tunnel();
        tunnel11.setLocalIp("10.1.50.2");
        tunnel11.setRemoteIp("10.2.50.2");
        Tunnel tunnel12 = new Tunnel();
        tunnel12.setLocalIp("10.1.60.2");
        tunnel12.setRemoteIp("10.2.60.2");
        site1.setTunnels(new Tunnel[]{tunnel11, tunnel12});

        Queue queue1 = new Queue();
        queue1.setId(1);
        queue1.setMaxRate("80%");
        queue1.setMinRate("20%");
        queue1.setPriority("max");
        queue1.setTosList(new String[]{"155", "255"});
        site1.setQueues(new Queue[]{queue1});

        topology.setSites(new Site[]{site1});

        final JAXBContext jaxbContext = JAXBContext.newInstance(Topology.class);
        final Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(topology, System.out);

    }


}
