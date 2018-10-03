package com.huawei.sdn.commons.selector.routing;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.huawei.sdn.commons.config.system.OsNetworkHelper;
import com.huawei.sdn.commons.config.topology.TopologyServiceHelper;
import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.db.EventDbController;
import com.huawei.sdn.commons.db.RoutingDataController;
import com.huawei.sdn.commons.db.model.EventCategory;
import com.huawei.sdn.commons.db.model.EventStatus;
import com.huawei.sdn.commons.db.model.Severity;
import com.huawei.sdn.commons.db.model.StaticRoute;
import com.huawei.sdn.commons.enums.ConnectorType;
import com.huawei.sdn.commons.selector.csp.PathSelectorSolver;
import com.huawei.sdn.commons.selector.routing.message.RouteInfo;
import com.huawei.sdn.commons.tools.NetUtils;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by oWX212574 on 6/9/2014.
 */
@Component("routeSelector")
public class RouteSelectorImpl implements RouteSelector, RouteConfigurator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteSelectorImpl.class);

    private ConcurrentSkipListMap<Integer, RouteEntry> routeTree = new ConcurrentSkipListMap<>();

    private AtomicInteger connectorIdProvider = new AtomicInteger(10000);
    private Map<String, Set<String>> tunnelInSiteMap = new ConcurrentHashMap<>();
    private Map<String, PSConnector> psConnectorMap = new ConcurrentHashMap<>();
    private Set<PSConnector> defaultGatewaySet = new HashSet<>();
    private Set<InetAddress> localIpAddressSet = new HashSet<>();

    private Random r = new Random();

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private PathSelectorSolver pathSelectorSolver;

    @Autowired
    private EventDbController eventDbController;

    @Autowired
    private TopologyServiceHelper topologyServiceHelper;

    @Autowired
    private OsNetworkHelper osNetworkHelper;

    @Autowired
    private RoutingDataController routingDataController;

    private Set<PSConnector> getConnectorsOrDefault(String address) {

        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        return getConnectorsOrDefault(inetAddress);

    }


    private Set<PSConnector> getConnectorsOrDefault(InetAddress address) {

        Set<PSConnector> connectors = getConnectors(address);

        if (connectors == null) {
            return Sets.newHashSet(defaultGatewaySet);
        }

        return Sets.newHashSet(connectors);

    }

    private Set<PSConnector> getConnectors(String address) {

        return getConnectors(NetUtils.parseInetAddress(address));

    }

    private Set<PSConnector> getConnectors(InetAddress inetAddress) {

        int intAddress = NetUtils.getIpAddressAsInt(inetAddress);

        RouteEntry entry = routeTree.get(intAddress);

        if (entry != null) {
            return entry.getConnectorSet();
        }

        NavigableSet<Integer> keySet = routeTree.headMap(intAddress).descendingKeySet();

        for (Integer key : keySet) {

            entry = routeTree.get(key);

            if (entry.getRouteInfo().getInfo().isInRange(inetAddress.getHostAddress())) {
                return entry.getConnectorSet();
            }

        }

        return null;

    }


    public String getCidrForLocalAddress(String ipAddress) {

        InetAddress inetAddress = NetUtils.parseInetAddress(ipAddress);

        int intAddress = NetUtils.getIpAddressAsInt(inetAddress);

        Map.Entry<Integer, RouteEntry> entry = routeTree.floorEntry(intAddress);

        if (entry == null || !entry.getValue().getRouteInfo().getInfo().isInRange(inetAddress.getHostAddress())) {
            return null;

        }

        return entry.getValue().getRouteInfo().getInfo().getCidrSignature();

    }

    private String getCidrForLocalAddress(InetAddress ipAddress) {

        int intAddress = NetUtils.getIpAddressAsInt(ipAddress);

        Map.Entry<Integer, RouteEntry> entry = routeTree.floorEntry(intAddress);

        if (entry == null || !entry.getValue().getRouteInfo().getInfo().isInRange(ipAddress.getHostAddress())) {
            return null;

        }

        return entry.getValue().getRouteInfo().getInfo().getCidrSignature();

    }


    @Override
    public PSConnector getConnector(String srcIpAddress, String dstIpAddress, boolean checkStatus) {

        Set<PSConnector> connectors = this.getConnectorsOrDefault(dstIpAddress);

        if (this.isSelfIpAddress(srcIpAddress)) {
            for (PSConnector connector : connectors) {
                if (connector.getLocalIPAddress().equals(srcIpAddress)) {
                    LOGGER.debug("packet was sent from the switch. selecting connector id: " + connector.getId());
                    return connector;
                }
            }

        }

        if (checkStatus) {
            Iterator<PSConnector> iterator = connectors.iterator();
            while (iterator.hasNext()) {
                PSConnector connector = iterator.next();
                if (!connector.isActive()) {
                    LOGGER.debug("removing inactive connector ID: " + connector.getId());
                    iterator.remove();
                }
            }
        }

        if (connectors.size() == 0) {
            LOGGER.error("There are no connectors to return.");
            return null;
        }

        return Lists.newArrayList(connectors).get(r.nextInt(connectors.size()));
    }

    @Override
    public boolean isConnected(String ipAddress) {

        Set<PSConnector> connectors = getConnectors(ipAddress);
        if (connectors == null || connectors.size() < 1) {
            return false;
        }

        return connectors.iterator().next().getType() == ConnectorType.LOCAL;

    }

    @Override
    public boolean isSelfIpAddress(String ipAddress) {

        return localIpAddressSet.contains(NetUtils.parseInetAddress(ipAddress));

    }

    @Override
    public boolean isDefaultGateway(String ipAddress) {

        for (PSConnector connector : defaultGatewaySet) {
            if (connector.getNextHopIpAddress().equals(ipAddress)) {
                return true;
            }
        }

        return false;

    }

    @Override
    public void setDefaultRoutePortId(String ipAddress, int portId) {

        for (PSConnector connector : defaultGatewaySet) {
            if (connector.getNextHopIpAddress().equals(ipAddress)) {

                String oldId = connector.getId();
                String newId = portId + "";

                if (!oldId.equals(newId)) {
                    connector.setId(newId);
                    psConnectorMap.put(connector.getId(), connector);
                    psConnectorMap.remove(oldId);

                    pathSelectorSolver.modifyPSConnector(connector);

                }

                break;

            }
        }

    }

    public InetAddress getLocalIpForSubnet(InetAddress address) {

        Map.Entry<Integer, RouteEntry> entry = routeTree.floorEntry(NetUtils.getIpAddressAsInt(address));

        if (entry == null || !entry.getValue().getRouteInfo().getInfo().isInRange(address.getHostAddress())) {
            throw new RuntimeException("ip address not in local range");
        }

        return NetUtils.parseInetAddress(entry.getValue().getRouteInfo().getInfo().getAddress());

    }

    @Override
    public Set<InetAddress> getLocalIpAddresses() {

        return Sets.newHashSet(localIpAddressSet);

    }

    @Override
    public Set<String> getLocalSubnetSet() {

        Set<String> subnetSet = new HashSet<>();

        for (InetAddress inetAddress : localIpAddressSet) {

            String cidrForLocalAddress = getCidrForLocalAddress(inetAddress);

            if (cidrForLocalAddress == null) {
                LOGGER.error("IP Address " + inetAddress.getHostAddress() + " is not in route tree");
                continue;
            }

            subnetSet.add(cidrForLocalAddress);

        }

        return subnetSet;

    }

    @Override
    public Set<PSConnector> getAllConnectors() {

        Set<PSConnector> psConnectors = new HashSet<>();

        for (PSConnector connector : psConnectorMap.values()) {
            psConnectors.add(new PSConnector(connector));
        }

        return psConnectors;
    }

    @Override
    public PSConnector getConnector(String id) {

        return psConnectorMap.get(id);

    }

    @Override
    public void linkStatusChanged(final String connectorId, final boolean connected) {

        psConnectorMap.get(connectorId).setActive(connected);

        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {

                try {

                    if (connected) {

                        eventDbController.save(Severity.NORMAL, EventCategory.NETWORK, "Connector id " + connectorId + " is up", EventStatus.NEW);

                    } else {

                        eventDbController.save(Severity.MAJOR, EventCategory.NETWORK, "Connector id " + connectorId + " is down", EventStatus.NEW);

                    }

                } catch (Exception e) {
                    LOGGER.error("Got Exception", e);

                }

            }
        });

    }

    @Override
    public List<PSConnector> getDefaultRoutes() {
        return Lists.newArrayList(defaultGatewaySet);
    }

    @Override
    public List<String> getDefaultRoutesIdList() {

        List<String> idList = new ArrayList<>();

        for (PSConnector connector : defaultGatewaySet) {
            idList.add(connector.getId());
        }

        return idList;

    }

    private PSConnector getDefaultForLocalIp(String localIpAddress) {

        for (PSConnector connector : defaultGatewaySet) {

            if (connector.getLocalIPAddress().equals(localIpAddress)) {
                return connector;
            }
        }

        return null;
    }


    @Override
    public List<PSConnector> getTunnelConnectorList() {

        List<PSConnector> tunnelConnectorList = new ArrayList<>();

        Collection<PSConnector> psConnectors = psConnectorMap.values();

        for (PSConnector psConnector : psConnectors) {

            if (psConnector.getType() == ConnectorType.GRE) {
                tunnelConnectorList.add(psConnector);
            }

        }

        return tunnelConnectorList;
    }

    @Override
    public List<String> getRemoteSiteIdList() {

        List<String> remoteSiteIdList = new ArrayList<>();

        remoteSiteIdList.addAll(tunnelInSiteMap.keySet());

        return remoteSiteIdList;

    }

    @Override
    public Map<String, Set<String>> getConnectorIdPerTunnel() {

        Map<String, Set<String>> connectorIdPerTunnel = new HashMap<>();

        Set<String> connectorIdSet;
        for (String siteId : tunnelInSiteMap.keySet()) {

            connectorIdSet = new HashSet<>();

            connectorIdSet.addAll(tunnelInSiteMap.get(siteId));

            connectorIdPerTunnel.put(siteId, connectorIdSet);

        }

        return connectorIdPerTunnel;

    }

    @Override
    public RouteInfo getFullRouteInfo() {

        RouteInfo routeInfo = this.getRouteInfoEntity();

        Map<String, Set<String>> subnetInSite = new HashMap<>();

        Set<RouteEntry> routeEntrySet = this.getRouteEntrySet();

        for (RouteEntry routeEntry : routeEntrySet) {

            String groupId = routeEntry.getConnectorSet().iterator().next().getGroupId();

            if (groupId.equals("")) {
                continue;
            }

            if (subnetInSite.containsKey(groupId)) {
                subnetInSite.get(groupId).add(routeEntry.getRouteInfo().getInfo().getCidrSignature());
            } else {
                subnetInSite.put(groupId, Sets.newHashSet(routeEntry.getRouteInfo().getInfo().getCidrSignature()));
            }
        }

        for (String siteId : subnetInSite.keySet()) {

            RouteInfo remoteRouteInfo = new RouteInfo();
            remoteRouteInfo.setSiteId(siteId);
            remoteRouteInfo.setSubnetSet(subnetInSite.get(siteId));
            routeInfo.addRemoteSiteRouteInfo(remoteRouteInfo);

        }

        return routeInfo;

    }

    @Override
    public RouteInfo getRouteInfo() {

        RouteInfo routeInfo = this.getRouteInfoEntity();

        List<StaticRoute> staticRouteList = routingDataController.getStaticRouteList();

        for (StaticRoute staticRoute : staticRouteList) {

            routeInfo.getSubnetSet().add(staticRoute.getDestIp() + "/" + staticRoute.getDestSubnet());

        }

        return routeInfo;
    }


    private RouteInfo getRouteInfoEntity(){

        String siteName = topologyServiceHelper.getNodeId();

        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setSiteId(siteName);
        routeInfo.setSubnetSet(this.getLocalSubnetSet());
        routeInfo.setMacAddress(NetUtils.bytesToHexString(topologyServiceHelper.getNodeMacAddress()));

        return routeInfo;

    }



    @Override
    public PSConnector setLocalIpAddress(String cidr) {

        LOGGER.debug("adding new local IP address: " + cidr);

        SubnetUtils subnetUtils = new SubnetUtils(cidr);
        InetAddress inetAddress = NetUtils.parseInetAddress(subnetUtils.getInfo().getAddress());

        PSConnector connector = new PSConnector(connectorIdProvider.getAndIncrement() + "", ConnectorType.LOCAL);
        connector.setLocalIPAddress(subnetUtils.getInfo().getAddress());
        connector.setLocalMacAddress(osNetworkHelper.getMacAddress(connector.getLocalIPAddress()));

        psConnectorMap.put(connector.getId(), connector);

        RouteEntry routeEntry = new RouteEntry(subnetUtils, Sets.newHashSet(connector));

        localIpAddressSet.add(inetAddress);
        routeTree.put(routeEntry.getNetworkAddress(), routeEntry);

        return connector;

    }

    @Override
    public PSConnector setStaticRoute(String destCidr, String nextHopIpAddress) {

        InetAddress nextHopInetAddress = NetUtils.parseInetAddress(nextHopIpAddress);

        Map.Entry<Integer, RouteEntry> entry = routeTree.floorEntry(NetUtils.getIpAddressAsInt(nextHopInetAddress));

        if (entry == null || !entry.getValue().getRouteInfo().getInfo().isInRange(nextHopIpAddress)) {
            throw new RuntimeException("next hop in un-reachable");
        }

        SubnetUtils subnetUtils = new SubnetUtils(destCidr);

        RouteEntry routeEntry = routeTree.get(NetUtils.getIpAddressStringAsInt(subnetUtils.getInfo().getNetworkAddress()));

        if (routeEntry == null || !routeEntry.getRouteInfo().getInfo().isInRange(subnetUtils.getInfo().getLowAddress())) {
            routeEntry = new RouteEntry(subnetUtils);
            routeTree.put(NetUtils.getIpAddressStringAsInt(subnetUtils.getInfo().getNetworkAddress()), routeEntry);
        }

        PSConnector connector = new PSConnector(connectorIdProvider.getAndIncrement() + "", nextHopIpAddress, ConnectorType.REMOTE);
        connector.setGroupId(destCidr);

        String localIPAddress = entry.getValue().getConnectorSet().iterator().next().getLocalIPAddress();

        connector.setLocalIPAddress(localIPAddress);

        connector.setLocalMacAddress(osNetworkHelper.getMacAddress(localIPAddress));

        psConnectorMap.put(connector.getId(), connector);

        routeEntry.getConnectorSet().add(connector);

        return connector;

    }

    @Override
    public void setTunnelStaticRoute(String siteId, String destCidr) {

        Set<String> tunnelIdSet = tunnelInSiteMap.get(siteId);

        if (tunnelIdSet == null || tunnelIdSet.isEmpty()) {
            throw new RuntimeException("there are no tunnels configured for site id " + siteId);
        }

        SubnetUtils subnetUtils = new SubnetUtils(destCidr);

        if(this.isConnected(subnetUtils.getInfo().getAddress())){
            LOGGER.debug("got from remote ip address that is connected: " + destCidr + ". skipping");
            return;
        }

        int networkAddressAsInt = NetUtils.getIpAddressStringAsInt(subnetUtils.getInfo().getNetworkAddress());
        if (routeTree.containsKey(networkAddressAsInt) && routeTree.get(networkAddressAsInt).getConnectorSet().size() == tunnelIdSet.size()) {
            return;
        }

        LOGGER.debug("Adding subnet " + destCidr + " to tunnel ");

        Set<PSConnector> connectorSet = new HashSet<>();

        for (String tunnelId : tunnelIdSet) {
            connectorSet.add(psConnectorMap.get(tunnelId));
        }

        RouteEntry routeEntry = new RouteEntry(subnetUtils, connectorSet);

        routeTree.put(networkAddressAsInt, routeEntry);

    }

    @Override
    public PSConnector setTunnel(String tunnelId, String localIpAddress, String remoteIpAddress) {

        LOGGER.debug("Adding tunnel id: " + tunnelId + ", local address: " + localIpAddress + ", remote address: " + remoteIpAddress);

        if (!isSelfIpAddress(localIpAddress)) {
            throw new RuntimeException("Local IP address not configured.");
        }

        PSConnector defaultForLocalIp = this.getDefaultForLocalIp(localIpAddress);

        if (defaultForLocalIp == null) {
            throw new RuntimeException("There is no default route for the local IP address");
        }

        PSConnector connector = new PSConnector(tunnelId, ConnectorType.GRE);
        connector.setLocalIPAddress(localIpAddress);
        connector.setDestinationAddress(remoteIpAddress);
        connector.setMovable(true);

        connector.setNextHopIpAddress(this.getDefaultForLocalIp(localIpAddress).getNextHopIpAddress());

        psConnectorMap.put(connector.getId(), connector);

        this.setStaticRoute(remoteIpAddress + "/32", defaultForLocalIp.getNextHopIpAddress());

        return connector;
    }

    @Override
    public void setSiteToTunnel(String siteId, String tunnelId) {

        Set<String> siteTunnels = tunnelInSiteMap.get(siteId);
        if (siteTunnels == null) {
            siteTunnels = new HashSet<>();
            siteTunnels.add(tunnelId);
            tunnelInSiteMap.put(siteId, siteTunnels);
            psConnectorMap.get(tunnelId).setGroupId(siteId);
        } else {
            siteTunnels.add(tunnelId);
            psConnectorMap.get(tunnelId).setGroupId(siteId);
        }

    }

    @Override
    public PSConnector setDefaultGateway(String ipAddress) {

        LOGGER.debug("Adding default gateway: " + ipAddress);

        Map.Entry<Integer, RouteEntry> entry = routeTree.floorEntry(NetUtils.getIpAddressStringAsInt(ipAddress));

        if (entry == null || !entry.getValue().getRouteInfo().getInfo().isInRange(ipAddress)) {
            throw new RuntimeException("next hop in un-reachable");
        }

        PSConnector defaultConnector = new PSConnector(connectorIdProvider.getAndIncrement() + "", ConnectorType.REMOTE);
        defaultConnector.setGroupId("0.0.0.0");
        String localIpAddress = entry.getValue().getRouteInfo().getInfo().getAddress();
        defaultConnector.setLocalIPAddress(localIpAddress);
        defaultConnector.setNextHopIpAddress(ipAddress);
        defaultConnector.setLocalMacAddress(osNetworkHelper.getMacAddress(localIpAddress));

        defaultGatewaySet.add(defaultConnector);
        psConnectorMap.put(defaultConnector.getId(), defaultConnector);

        return defaultConnector;


    }

    @Override
    public Set<RouteEntry> getRouteEntrySet() {
        return Sets.newHashSet(routeTree.values());
    }


    private void detectNewTunnels() {

        //  check that all tunnels are connected to site
        for (final PSConnector connector : psConnectorMap.values()) {

            if (connector.getType() == ConnectorType.GRE && connector.getGroupId().equals("")) {

                taskExecutor.execute(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            RouteInfo routeInfo = getRouteInfo(connector);

                            setNewTunnelForSite(routeInfo, connector);

                        } catch (Exception e) {
                            LOGGER.error("Got Exception: ", e);
                        }


                    }
                });

            }

        }


    }

    public void setNewTunnelForSite(RouteInfo routeInfo, PSConnector connector) {

        if (routeInfo != null) {

            LOGGER.debug("Got " + routeInfo);
            LOGGER.debug("connecting tunnelId: " + connector.getId() + " to siteId: " + connector.getGroupId());
            connector.setGroupId(routeInfo.getSiteId());
            connector.setRemoteMacAddress(NetUtils.hexStringToByteArray(routeInfo.getMacAddress()));
            setSiteToTunnel(connector.getGroupId(), connector.getId());

            pathSelectorSolver.modifyPSConnector(connector);

            addStaticRoute(routeInfo);

        }

    }


    private RouteInfo getRouteInfo(PSConnector tunnelConnector) {

//        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        RouteInfo routeInfo = null;

        try {

            RequestConfig requestConfig = RequestConfig.custom()
                    .setLocalAddress(InetAddress.getByName(tunnelConnector.getLocalIPAddress()))
                    .setConnectTimeout(10000)
                    .build();

            HttpGet httpget = new HttpGet("http://" + tunnelConnector.getDestinationAddress() + ":8080/pathselector/routing/info");
            httpget.setConfig(requestConfig);
            CloseableHttpClient httpclient = HttpClients.createDefault();
            CloseableHttpResponse response = httpclient.execute(httpget);

            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() != 200) {
                return null;
            }

            HttpEntity entity = response.getEntity();

            routeInfo = mapper.readValue(entity.getContent(), RouteInfo.class);

        } catch (HttpHostConnectException e){
            LOGGER.warn("Did not get HTTP response from " + tunnelConnector.getDestinationAddress() + ", reason: host connect exception ");
        } catch (ConnectTimeoutException e) {
            LOGGER.warn("Did not get HTTP response from " + tunnelConnector.getDestinationAddress() + ", reason: connection timeout ");
        } catch (RuntimeException e) {
            LOGGER.warn("Did not get HTTP response from " + tunnelConnector.getDestinationAddress());
        } catch (ClientProtocolException e) {
            LOGGER.error("Got Exception: ", e);
        } catch (IOException e) {
            LOGGER.error("Got Exception: ", e);
        }

        return routeInfo;

    }

    private void addStaticRoute(RouteInfo routeInfo) {

//        add connected subnets to the reporting site

        String reportingSiteId = routeInfo.getSiteId();

        for (String cidr : routeInfo.getSubnetSet()) {
            RouteSelectorImpl.this.setTunnelStaticRoute(reportingSiteId, cidr);
        }

//        add remote subnets to the reporting site

        String nodeId = topologyServiceHelper.getNodeId();

        for (RouteInfo remoteSiteInfo : routeInfo.getRemoteSiteSet()) {

            if(remoteSiteInfo.getSiteId().equals(nodeId)){
                continue;
            }

            for (String remoteSiteSubnet : remoteSiteInfo.getSubnetSet()) {

                RouteSelectorImpl.this.setTunnelStaticRoute(reportingSiteId, remoteSiteSubnet);

            }

        }

    }

    private void updateTunnels() {

        for (final String siteId : tunnelInSiteMap.keySet()) {

            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {

                    try {

                        String tunnelId = tunnelInSiteMap.get(siteId).iterator().next();

                        LOGGER.debug("Getting remote subnets for tunnel id: " + tunnelId);

                        PSConnector tunnelConnector = psConnectorMap.get(tunnelId);

                        if (!tunnelConnector.getGroupId().equals(siteId)) {

                            tunnelConnector.setGroupId(siteId);

                        }

                        RouteInfo routeInfo = getRouteInfo(tunnelConnector);

                        if (routeInfo != null) {

                            LOGGER.debug("Got " + routeInfo);

                            addStaticRoute(routeInfo);

                        }

                    } catch (RuntimeException e) {
                        LOGGER.error("Got Exception", e);
                    }

                }
            });

        }


    }

    @PostConstruct
    private void getRoutesFromPeers() {

        taskScheduler.schedule(new Runnable() {
            @Override
            public void run() {

                detectNewTunnels();

                updateTunnels();

            }

        }, new CronTrigger("0/10 * * * * ?"));


    }

}
