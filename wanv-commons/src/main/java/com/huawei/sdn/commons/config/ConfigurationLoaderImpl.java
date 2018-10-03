package com.huawei.sdn.commons.config;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.huawei.sdn.commons.config.system.OsNetworkHelper;
import com.huawei.sdn.commons.config.topology.*;
import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.db.RoutingDataController;
import com.huawei.sdn.commons.db.model.*;
import com.huawei.sdn.commons.db.model.Tunnel;
import com.huawei.sdn.commons.selector.csp.PathSelectorSolver;
import com.huawei.sdn.commons.selector.routing.RouteConfigurator;
import net.sf.cglib.util.ParallelSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Configuration XML file helper class
 */
@Component("configurationLoader")
public final class ConfigurationLoaderImpl implements ConfigurationLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationLoaderImpl.class);
    private Topology topology;

    @Autowired
    public ConfigurationLoaderImpl(@Value("${TOPOLOGY_FILE}") String topologyXmlFile) {

        this.load(topologyXmlFile);

    }


    @Autowired
    private RoutingDataController routingDataController;

    @Autowired
    private OsNetworkHelper osNetworkHelper;

    @Autowired
    private RouteConfigurator routeConfigurator;

    @Autowired
    private PathSelectorSolver pathSelectorSolver;

    @Autowired
    private TopologyServiceHelper topologyServiceHelper;

    @Autowired
    private OamManagerConfig oamManagerConfig;


    /**
     * load a XML configuration to a XML object
     *
     * @param topologyXmlFile the XML configuration file
     * @return the XML object Configuration corresponding to the XML file
     */
    @Override
    public Topology load(String topologyXmlFile) {

        File topologyFile = new File(topologyXmlFile);

        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(Topology.class);

            final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            topology = (Topology) jaxbUnmarshaller.unmarshal(topologyFile);

            return topology;

        } catch (final JAXBException e) {
            LOGGER.error("error during loading configuration.", e);
            return null;
        }
    }

    @Override
    public Topology getTopology() {
        return topology;
    }

    @Override
    public String getReferenceUrl() {
        return topology.getConfig().getReferenceUrl();
    }

    @Override
    public int getTimeout() {
        return topology.getConfig().getTimeout();
    }

    @Override
    public void setTopology(Topology topology) {
        this.topology = topology;
    }

    @Override
    public Operation getHeartBitOperation() {
        return topology.getHeartBitOperation();
    }

    @Override
    public Operation getDelayOperation() {
        return topology.getDelayOperation();
    }

    @Override
    public Set<Queue> getQueueSet() {
        Queue[] queues = topology.getQueues();

        if (queues.length == 0) {
            return new HashSet<>();
        }

        Set<Queue> queueSet = new HashSet<>();
        queueSet.addAll(Arrays.asList(queues));

        return queueSet;

    }

    @Override
    public Queue getDefaultQueue() {

        Set<Queue> queueSet = this.getQueueSet();

        for (Queue queue : queueSet) {
            if (queue.isDefaultQueue()) {
                return queue;
            }
        }

        return null;

    }

    public void init() {

        LOGGER.debug("Adding local IP addresses");

        String pattern = "\\d{0,3}\\.\\d{0,3}\\.\\d{0,3}\\.\\d{0,3}/\\d{1,2}";

        List<String> allBridgeCidr = osNetworkHelper.getAllBridgeCidr();

        for (String cidr : allBridgeCidr) {

            if(!cidr.matches(pattern)) {
                continue;
            }

            PSConnector connector = routeConfigurator.setLocalIpAddress(cidr);

            pathSelectorSolver.addPSConnector(connector);

        }


        LOGGER.debug("Adding Default Gateways");

        List<Gateway> gatewayList = routingDataController.getGatewayList();

        for (Gateway gateway : gatewayList) {

            PSConnector connector = routeConfigurator.setDefaultGateway(gateway.getIp_address());

            pathSelectorSolver.addPSConnector(connector);

        }

        LOGGER.debug("Adding Tunnels");

        for (Tunnel tunnel : routingDataController.getTunnelList()) {

            this.addTunnel(tunnel.getId()+"",tunnel.getLocalIp(),tunnel.getRemoteIp());

        }

        LOGGER.debug("Adding Static Routes");

        for (StaticRoute staticRoute : routingDataController.getStaticRouteList()) {

            this.addStaticRoute(staticRoute.getDestIp(),staticRoute.getDestSubnet(),staticRoute.getNextHop());

        }


    }

    public void setDefaultGateway(String ip) {

        routingDataController.saveDefaultGateway(ip);

        PSConnector connector = routeConfigurator.setDefaultGateway(ip);

        pathSelectorSolver.addPSConnector(connector);

    }


    public void setLocalIpAddress(String localIp, String localSubnet) {

        routingDataController.saveLocalIpAddress(localIp, localSubnet);

        PSConnector connector = routeConfigurator.setLocalIpAddress(localIp + "/" + localSubnet);

        pathSelectorSolver.addPSConnector(connector);

    }

    public void setTunnel(String tunnelId, String localIp, String remoteIp) {

        routingDataController.saveTunnel(tunnelId, localIp, remoteIp);

        this.addTunnel(tunnelId, localIp, remoteIp);

    }


    private void addTunnel(String tunnelId, String localIp, String remoteIp){

        PSConnector connector = routeConfigurator.setTunnel(tunnelId, localIp, remoteIp);
        connector.setLocalMacAddress(topologyServiceHelper.getNodeMacAddress());

        pathSelectorSolver.addPSConnector(connector);

        oamManagerConfig.greConnectorAdded(connector);

    }



    @Override
    public void setStaticRoute(String destIp, String destSubnet, String nextHop) {

        routingDataController.saveStaticRoute(destIp, destSubnet, nextHop);

        this.addStaticRoute(destIp, destSubnet, nextHop);

    }


    private void addStaticRoute(String destIp, String destSubnet, String nextHop) {

        PSConnector connector = routeConfigurator.setStaticRoute(destIp + "/" + destSubnet, nextHop);

        pathSelectorSolver.addPSConnector(connector);


    }



}
