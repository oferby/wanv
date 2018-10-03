package com.huawei.sdn.pathselector.odl.flow;

import com.huawei.sdn.commons.data.MacAddress;
import com.huawei.sdn.commons.tools.NetUtils;
import org.opendaylight.controller.sal.action.*;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.flowprogrammer.Flow;
import org.opendaylight.controller.sal.match.Match;
import org.opendaylight.controller.sal.match.MatchType;
import org.opendaylight.controller.sal.packet.*;
import org.opendaylight.controller.sal.utils.EtherTypes;
import org.opendaylight.controller.sal.utils.IPProtocols;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/22/14
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class FlowFactory {

    public static final AtomicLong flowIdGenerator = new AtomicLong(10000);
    private static final short FLOW_TIMEOUT = 20;

    public static Flow createArpFlow() {

        final Match match = new Match();
        match.setField(MatchType.DL_TYPE, EtherTypes.ARP.shortValue());

        final List<Action> actions = new ArrayList<>();
        actions.add(new Controller());
        actions.add(new HwPath());

        final Flow flow = new Flow(match, actions);
        flow.setId(flowIdGenerator.getAndIncrement());
        flow.setPriority((short) 4000);

        return flow;

    }

    public static Flow createDHCPFlow(short port) {

        final Match match = new Match();
        match.setField(MatchType.DL_TYPE, EtherTypes.IPv4.shortValue());
        match.setField(MatchType.NW_DST, NetUtils.parseInetAddress("255.255.255.255"), NetUtils.parseInetAddress("255.255.255.255"));
        match.setField(MatchType.NW_PROTO, IPProtocols.UDP.byteValue());
        match.setField(MatchType.TP_DST, port);

        final List<Action> actions = new ArrayList<>();
        actions.add(new HwPath());

        final Flow flow = new Flow(match, actions);
        flow.setId(flowIdGenerator.getAndIncrement());
        flow.setPriority((short) 4000);

        return flow;

    }


    public static Flow createGreFlow(long flowId, IPv4 iPv4, InetAddress dstAddress, NodeConnector nodeConnector) {

        final Match match = new Match();
        match.setField(MatchType.DL_TYPE, EtherTypes.IPv4.shortValue());

        match.setField(MatchType.NW_SRC, NetUtils.getInetAddress(iPv4.getSourceAddress()), NetUtils.parseInetAddress("255.255.255.255"));
        match.setField(MatchType.NW_DST, dstAddress, NetUtils.parseInetAddress("255.255.255.255"));

        if (iPv4.getDiffServ() != 0) {
            match.setField(MatchType.NW_TOS, iPv4.getDiffServ());
        }

        Packet payload = iPv4.getPayload();

        boolean haveProtocol = false;
        if (payload instanceof TCP) {
            haveProtocol = true;
            match.setField(MatchType.NW_PROTO, IPProtocols.TCP.byteValue());
            TCP tcp = (TCP) payload;
            match.setField(MatchType.TP_SRC, tcp.getSourcePort());
            match.setField(MatchType.TP_DST, tcp.getDestinationPort());

        } else if (payload instanceof UDP) {
            haveProtocol = true;
            match.setField(MatchType.NW_PROTO, IPProtocols.UDP.byteValue());
            UDP udp = (UDP) payload;
            match.setField(MatchType.TP_SRC, udp.getSourcePort());
            match.setField(MatchType.TP_DST, udp.getDestinationPort());

        } else if (payload instanceof ICMP) {
            haveProtocol = true;
            match.setField(MatchType.NW_PROTO, IPProtocols.ICMP.byteValue());
        }

        final List<Action> actions = new ArrayList<>();

        actions.add(new Enqueue(nodeConnector, 0));

        final Flow flow = new Flow(match, actions);
        if (haveProtocol)
            flow.setPriority((short) 310);
        else
            flow.setPriority((short) 300);
        flow.setIdleTimeout(FLOW_TIMEOUT);

        flow.setId(flowId);
        return flow;

    }

    public static Flow createGreIncomingFlow(long flowId, InetAddress src, InetAddress dstAddress) {

        final Match match = new Match();
        match.setField(MatchType.DL_TYPE, EtherTypes.IPv4.shortValue());

//        match.setField(MatchType.NW_SRC, src, NetUtils.parseInetAddress("255.255.255.255"));
        match.setField(MatchType.NW_DST, dstAddress, NetUtils.parseInetAddress("255.255.255.255"));

//        match.setField(MatchType.NW_PROTO, IPProtocols.GRE.byteValue());

        final List<Action> actions = new ArrayList<>();

        actions.add(new HwPath());

        final Flow flow = new Flow(match, actions);
        flow.setPriority((short) 3000);
        flow.setIdleTimeout(FLOW_TIMEOUT);
        flow.setId(flowId);

        return flow;

    }

    public static Flow createGreOutgoingFlow(long flowId, InetAddress src, InetAddress dstAddress, byte[] sMac, byte[] dMac, NodeConnector nodeConnector) {

        final Match match = new Match();
        match.setField(MatchType.DL_TYPE, EtherTypes.IPv4.shortValue());

        match.setField(MatchType.NW_SRC, src, NetUtils.parseInetAddress("255.255.255.255"));
        match.setField(MatchType.NW_DST, dstAddress, NetUtils.parseInetAddress("255.255.255.255"));

//        match.setField(MatchType.NW_PROTO, IPProtocols.GRE.byteValue());

        final List<Action> actions = new ArrayList<>();

        actions.add(new SetDlSrc(sMac));
        actions.add(new SetDlDst(dMac));
        actions.add(new Enqueue(nodeConnector, 0));

        final Flow flow = new Flow(match, actions);
        flow.setPriority((short) 3001);
        flow.setIdleTimeout(FLOW_TIMEOUT);
        flow.setId(flowId);

        return flow;

    }


    public static Flow createMacBasedLocalFlow(long flowId, Ethernet packet, NodeConnector outNodeConnector) {
        final Match match = new Match();
        match.setField(MatchType.DL_DST, packet.getSourceMACAddress());

        final List<Action> actions = new ArrayList<>();
        actions.add(new Enqueue(outNodeConnector, 0));

        Flow flow = new Flow(match, actions);
        flow.setPriority((short) 600);
        flow.setIdleTimeout(FLOW_TIMEOUT);

        flow.setId(flowId);
        return flow;

    }

    public static Flow createConnectedIpFlow(InetAddress src, InetAddress dst, byte[] sMac, byte[] dMac, NodeConnector nodeConnector) {

        Match match = new Match();
        match.setField(MatchType.DL_TYPE, EtherTypes.IPv4.shortValue());
        match.setField(MatchType.NW_DST, dst, NetUtils.parseInetAddress("255.255.255.255"));

        final List<Action> actions = new ArrayList<>();

        if (dMac == null) {
            match.setField(MatchType.NW_SRC, src, NetUtils.parseInetAddress("255.255.255.255"));
            actions.add(new HwPath());
        } else {

            actions.add(new SetDlSrc(sMac));
            actions.add(new SetDlDst(dMac));
            actions.add(new Enqueue(nodeConnector, 0));

        }

        Flow flow = new Flow(match, actions);
        flow.setPriority((short) 500);
        flow.setIdleTimeout((short) 300);

        flow.setId(flowIdGenerator.incrementAndGet());
        return flow;
    }


    public static Flow createConnectedIpFlow(InetAddress src, InetAddress dst, byte[] sMac, byte[] dMac, NodeConnector nodeConnector, IPv4 iPv4) {

        Match match = new Match();
        match.setField(MatchType.DL_TYPE, EtherTypes.IPv4.shortValue());
        match.setField(MatchType.NW_DST, dst, NetUtils.parseInetAddress("255.255.255.255"));

//        Packet payload = iPv4.getPayload();

//        if (payload instanceof TCP) {
//            match.setField(MatchType.NW_PROTO, IPProtocols.TCP.byteValue());
//            TCP tcp = (TCP) payload;
//            match.setField(MatchType.TP_SRC, tcp.getSourcePort());
//            match.setField(MatchType.TP_DST, tcp.getDestinationPort());
//
//        } else if (payload instanceof UDP) {
//            match.setField(MatchType.NW_PROTO, IPProtocols.UDP.byteValue());
//            UDP udp = (UDP) payload;
//            match.setField(MatchType.TP_SRC, udp.getSourcePort());
//            match.setField(MatchType.TP_DST, udp.getDestinationPort());
//
//        }

        final List<Action> actions = new ArrayList<>();

        if (dMac != null) {
//            match.setField(MatchType.NW_SRC, src, NetUtils.parseInetAddress("255.255.255.255"));
//            actions.add(new HwPath());
//        } else {

            actions.add(new SetDlSrc(sMac));
            actions.add(new SetDlDst(dMac));
            actions.add(new Enqueue(nodeConnector, 0));

        }

        Flow flow = new Flow(match, actions);
        flow.setPriority((short) 501);
        flow.setIdleTimeout(FLOW_TIMEOUT);

        flow.setId(flowIdGenerator.incrementAndGet());
        return flow;

    }


    public static Flow createDefaultGatewayFlow(IPv4 iPv4, byte[] sMac, MacAddress dMacAddress, NodeConnector nodeConnector, boolean useSrcPort) {

        Match match = new Match();
        match.setField(MatchType.DL_TYPE, EtherTypes.IPv4.shortValue());
        match.setField(MatchType.NW_SRC, NetUtils.getInetAddress(iPv4.getSourceAddress()), NetUtils.parseInetAddress("255.255.255.255"));
        match.setField(MatchType.NW_DST, NetUtils.getInetAddress(iPv4.getDestinationAddress()), NetUtils.parseInetAddress("255.255.255.255"));

        if (iPv4.getDiffServ() != 0) {
            match.setField(MatchType.NW_TOS, iPv4.getDiffServ());
        }

        Packet payload = iPv4.getPayload();

        boolean haveProtocol = false;
        if (payload instanceof TCP) {
            haveProtocol = true;
            match.setField(MatchType.NW_PROTO, IPProtocols.TCP.byteValue());
            TCP tcp = (TCP) payload;
            if(useSrcPort){
                match.setField(MatchType.TP_SRC, tcp.getSourcePort());
            }
            match.setField(MatchType.TP_DST, tcp.getDestinationPort());

        } else if (payload instanceof UDP) {
            haveProtocol = true;
            match.setField(MatchType.NW_PROTO, IPProtocols.UDP.byteValue());
            UDP udp = (UDP) payload;
            if(useSrcPort){
                match.setField(MatchType.TP_SRC, udp.getSourcePort());
            }

            match.setField(MatchType.TP_DST, udp.getDestinationPort());

        } else if (payload instanceof ICMP) {
            haveProtocol = true;
            match.setField(MatchType.NW_PROTO, IPProtocols.ICMP.byteValue());
        }

        final List<Action> actions = new ArrayList<>();

        actions.add(new SetDlSrc(sMac));
        actions.add(new SetDlDst(dMacAddress.getMac()));
        actions.add(new Enqueue(nodeConnector, 0));

        Flow flow = new Flow(match, actions);

        if (haveProtocol)
            flow.setPriority((short) 410);
        else
            flow.setPriority((short) 400);

        flow.setIdleTimeout(FLOW_TIMEOUT);

        flow.setId(flowIdGenerator.incrementAndGet());
        return flow;

    }

    public static Flow createDefaultGatewayFlow(byte[] sMac, byte[] dMac,
                                                InetAddress srcIp, InetAddress dstIp,
                                                Byte nwProto,
                                                Short tpSrc, Short tpDst,
                                                NodeConnector nodeConnector,
                                                int queueId,
                                                long flowId) {

        Match match = new Match();
        match.setField(MatchType.DL_TYPE, EtherTypes.IPv4.shortValue());
        match.setField(MatchType.NW_SRC, srcIp);
        match.setField(MatchType.NW_DST, dstIp);

        boolean haveProtocol = false;

        if (nwProto != null) {

            haveProtocol = true;

            match.setField(MatchType.NW_PROTO, nwProto);

            if (tpDst != null) {
                match.setField(MatchType.TP_DST, tpDst);

//                for DNS query we don't want the source port
                if (tpSrc != null && tpDst != 53) {
                    match.setField(MatchType.TP_SRC, tpSrc);
                }

            }

        }

        final List<Action> actions = new ArrayList<>();

        actions.add(new SetDlSrc(sMac));
        actions.add(new SetDlDst(dMac));
        actions.add(new Enqueue(nodeConnector, queueId));

        Flow flow = new Flow(match, actions);

        if (haveProtocol)
            flow.setPriority((short) 410);
        else
            flow.setPriority((short) 400);

        flow.setIdleTimeout(FLOW_TIMEOUT);

        flow.setId(flowId);
        return flow;


    }


}
