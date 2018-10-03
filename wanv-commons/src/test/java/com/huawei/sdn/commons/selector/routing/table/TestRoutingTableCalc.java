package com.huawei.sdn.commons.selector.routing.table;

import com.google.common.collect.Sets;
import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.enums.ConnectorType;
import com.huawei.sdn.commons.selector.routing.RouteEntry;
import org.apache.commons.net.util.SubnetUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by oWX212574 on 12/18/2014.
 */
@RunWith(JUnit4.class)
public class TestRoutingTableCalc {

    private TreeMap<Integer, RouteEntry> routeTree = new TreeMap<>();

    private AtomicInteger connectorId = new AtomicInteger(0);
    private Map<String,Set<PSConnector>>greTunnelConnectorMap = new HashMap<>();
    private Set<PSConnector>defaultGatewaySet=new HashSet<>();

    @Test
    public void testTable() throws UnknownHostException {

        setup();

        InetAddress address = Inet4Address.getByName("192.168.33.3");

        Set<PSConnector> connectorSet = getConnectorsOrDefault(address);

        assert (connectorSet.iterator().next().getType() == ConnectorType.GRE && connectorSet.size()==2);

        address = Inet4Address.getByName("192.168.100.3");

        connectorSet = getConnectorsOrDefault(address);

        assert (connectorSet.iterator().next().getType() == ConnectorType.LOCAL && connectorSet.size()==1);

        address = Inet4Address.getByName("192.168.200.3");

        connectorSet = getConnectorsOrDefault(address);

        assert (connectorSet.iterator().next().getType() == ConnectorType.GRE && connectorSet.size()==2);

        address = Inet4Address.getByName("192.168.201.1");

        connectorSet = getConnectorsOrDefault(address);

        assert (connectorSet.iterator().next().getType() == ConnectorType.REMOTE && connectorSet.size()==2);


        address = Inet4Address.getByName("192.168.101.1");

        connectorSet = getConnectorsOrDefault(address);
        PSConnector connector = connectorSet.iterator().next();
        assert (connector.getType() == ConnectorType.REMOTE && connector.getNextHopIpAddress().equals("192.168.100.55") && connectorSet.size()==1);


        address = Inet4Address.getByName("192.168.101.1");
        assert (!isSelfIpAddress(address));

        address = Inet4Address.getByName("192.168.100.200");
        assert (!isSelfIpAddress(address));

        address = Inet4Address.getByName("192.168.100.254");
        assert (isSelfIpAddress(address));


    }


    public Set<PSConnector> getConnectorsOrDefault(InetAddress address) {

        Set<PSConnector> connectors = getConnectors(address);

        if(connectors==null){
            return Sets.newHashSet(defaultGatewaySet);
        }

        return Sets.newHashSet(connectors);

    }


    private Set<PSConnector> getConnectors(InetAddress address) {

        int intAddress = ByteBuffer.wrap(address.getAddress()).getInt();

        Map.Entry<Integer, RouteEntry> entry = routeTree.floorEntry(intAddress);

        if (entry == null || !entry.getValue().getRouteInfo().getInfo().isInRange(address.getHostAddress())) {
            return null;

        }

        return entry.getValue().getConnectorSet();

    }



    private boolean isSelfIpAddress(InetAddress ipAddress) {

        Set<PSConnector> connectors = getConnectors(ipAddress);

        return connectors !=null && connectors.size()==1 && connectors.iterator().next().getLocalIPAddress().equals(ipAddress.getHostAddress());

    }


    private void setup() {

        setLocalIpAddress("192.168.100.254/24");

        setLocalIpAddress("10.1.50.2/30");

        setLocalIpAddress("10.1.60.2/30");

        setTunnels("site2",connectorId.getAndIncrement(),"10.1.50.2","10.2.50.2");
        setTunnels("site2",connectorId.getAndIncrement(),"10.1.60.2","10.2.60.2");

        setTunnels("site3",connectorId.getAndIncrement(),"10.1.50.2","10.1.70.2");
        setTunnels("site3",connectorId.getAndIncrement(),"10.1.60.2","10.2.70.2");

        setRemoteRouteEntry("192.168.200.0/24", "site2");
        setRemoteRouteEntry("192.168.33.0/24", "site3");

        setRemoteRouteEntry("192.168.101.0/24","192.168.100.55");

        setRemoteRouteEntry("0.0.0.0/0","10.1.50.1");
        setRemoteRouteEntry("0.0.0.0/0","10.1.60.1");

    }


    private void setLocalIpAddress(String cidr){

        RouteEntry routeEntry = new RouteEntry(new SubnetUtils(cidr));
        PSConnector psConnector = new PSConnector("" + connectorId.getAndIncrement(), ConnectorType.LOCAL);
        psConnector.setLocalIPAddress(routeEntry.getRouteInfo().getInfo().getAddress());
        routeEntry.getConnectorSet().add(psConnector);
        routeTree.put(routeEntry.getNetworkAddress(), routeEntry);

    }

    public void setTunnels(String siteId, int tunnelId, String localAddress, String remoteAddress){

        PSConnector connector = new PSConnector(""+tunnelId,ConnectorType.GRE);
        connector.setLocalIPAddress(localAddress);
        connector.setNextHopIpAddress(remoteAddress);
        connector.setGroupId(siteId);

        if(greTunnelConnectorMap.containsKey(siteId)){
            Set<PSConnector> connectors = greTunnelConnectorMap.get(siteId);
            connectors.add(connector);
        } else {
            Set<PSConnector>connectorSet=new HashSet<>();
            connectorSet.add(connector);
            greTunnelConnectorMap.put(siteId,connectorSet);
        }

    }


    private void setGreRouteEntry(String cidr, String siteId){

        Set<PSConnector> connectors = greTunnelConnectorMap.get(siteId);

        RouteEntry routeEntry = new RouteEntry(new SubnetUtils(cidr));
        routeEntry.getConnectorSet().addAll(connectors);
        routeTree.put(routeEntry.getNetworkAddress(), routeEntry);

    }

    private void setDefaultRouteEntry(String nextHope){

        InetAddress address;
        try {
            address = InetAddress.getByName(nextHope);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        Set<PSConnector> connectors = getConnectors(address);

        if(connectors==null || connectors.size()!=1){
            throw new RuntimeException("Invalid default route configuration");
        }

        PSConnector connector = new PSConnector(""+connectorId.getAndIncrement(),ConnectorType.REMOTE);
        connector.setGroupId("0.0.0.0");
        connector.setNextHopIpAddress(nextHope);
        connector.setLocalIPAddress(connectors.iterator().next().getLocalIPAddress());

        defaultGatewaySet.add(connector);

    }


    private void setRemoteRouteEntry(String cidr, String nextHope){

        if(cidr.equals("0.0.0.0/0")){
            setDefaultRouteEntry(nextHope);
            return;
        }

        if(greTunnelConnectorMap.containsKey(nextHope)){
            setGreRouteEntry(cidr,nextHope);
            return;
        }

        Set<PSConnector> connectors = null;
        InetAddress address = null;
        try {

            address = InetAddress.getByName(nextHope);

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        connectors = getConnectorsOrDefault(address);

        if(connectors==null || connectors.size()!=1){
            throw new RuntimeException("Invalid remote route configuration");
        }

        RouteEntry routeEntry = new RouteEntry(new SubnetUtils(cidr));
        PSConnector connector = new PSConnector(""+connectorId.getAndIncrement(),ConnectorType.REMOTE);
        connector.setLocalIPAddress(connectors.iterator().next().getLocalIPAddress());
        connector.setNextHopIpAddress(address.getHostAddress());
        connector.setGroupId(cidr);
        routeEntry.addConnector(connector);

        routeTree.put(routeEntry.getNetworkAddress(),routeEntry);

    }
//    213.200.108.32

    @Test
    public void testReverseDns() throws UnknownHostException {

        try {
            Hashtable env = new Hashtable();
            env.put("java.naming.factory.initial","com.sun.jndi.dns.DnsContextFactory");

            DirContext ctx = new InitialDirContext(env);
            Attributes attrs = ctx.getAttributes("219.219.61.23.in-addr.arpa",new String[] {"PTR"});

            for (NamingEnumeration ae = attrs.getAll();ae.hasMoreElements();) {
                Attribute attr = (Attribute)ae.next();
                String attrId = attr.getID();
                for (Enumeration vals = attr.getAll();vals.hasMoreElements();
                     System.out.println(attrId + ": " + vals.nextElement()));
            }

            ctx.close();
        }

        catch(Exception e) {
            System.out.println("NO REVERSE DNS");
        }

    }

}
