package com.huawei.sdn.commons.topology.manager;

import java.net.*;
import java.util.*;

import com.huawei.sdn.commons.config.ConfigurationLoader;
import com.huawei.sdn.commons.config.topology.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.sdn.commons.jms.properties.SystemProperties;
import com.huawei.sdn.commons.tools.NetUtils;
import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.data.PSPacketIn;
import com.huawei.sdn.commons.data.PSPacketIn.OfbMatchFields;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class represents a configuration manager for switch path selector
 */
//@Controller("switchConfigManager")
public class SwitchConfigManagerImpl implements SwitchConfigManager {
    @Autowired
    private ConfigurationLoader configurationLoader;
    private static final Logger LOGGER = LoggerFactory.getLogger(SwitchConfigManager.class);
    private PSConnector localLanConnector;
    private Map<String, PSConnector> greConnectors;
    private SystemProperties systemProperties;
    private Topology topology;
    private Site currentSite;
    private String serverName;
    private byte[] macSwitch;
    private Set<OfbMatchFields> ofbMatchFields;

    public SwitchConfigManagerImpl() {
        int i = 1;
    }

    @Override
    public void clear() {
        localLanConnector = null;
        greConnectors = null;
        systemProperties = null;
        ofbMatchFields = null;
    }

    /**
     * Get a local Network Interface
     *
     * @param name
     *            The name of the Network Interface (ie: br-eth1)
     * @return The Java NetWork Interface Object
     * @throws SocketException
     *             if an I/O error occurs
     */
    protected NetworkInterface getNetworkInterface(String name) throws SocketException {
        final Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
        while (nis.hasMoreElements()) {
            final NetworkInterface networkInterface = nis.nextElement();
            if (name != null && name.equalsIgnoreCase(networkInterface.getName())) {
                return networkInterface;
            }
        }
        return null;
    }

    protected NetworkInterface getNetworkInterface(InetAddress address) throws SocketException {
        final Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
        while (nis.hasMoreElements()) {
            final NetworkInterface networkInterface = nis.nextElement();
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while(addresses.hasMoreElements()) {
                if(addresses.nextElement().equals(address)) { // check range?
                    return networkInterface;
                }
            }
        }
        return null;
    }

    /**
     * trim a string taking in account the null value
     *
     * @param in
     *            the string to trim
     * @return the trimmed string
     */
    protected String trim(String in) {
        if (in == null) {
            return null;
        }
        return in.trim();
    }

    /**
     * Upper case a string taking in account the null value
     *
     * @param in
     *            the string to upper
     * @return the string upper case
     */
    protected String toUpperCase(String in) {
        if (in == null) {
            return null;
        }
        return in.toUpperCase();
    }



    @Override
    public void loadConfiguration(String topologyFileName, String systemPropertiesFileName) {
        try {
            clear();
            currentSite = retrieveSite();
            System.setProperty("currentSite", currentSite.getId());

            if(topology.getSwitchName() != null) {
                setMacSwitch(getNetworkInterface(topology.getSwitchName()).getHardwareAddress());
            }
            systemProperties = new SystemProperties(systemPropertiesFileName);
            // define the default fields: NW_SRC, NW_DST, TP_DST
            final String matchFields = systemProperties.getMatchFields() == null ? "NW_SRC, NW_DST, NW_PROTO, TP_DST"
                    : systemProperties.getMatchFields();
            final Set<OfbMatchFields> ofbMatchFields = new HashSet<PSPacketIn.OfbMatchFields>();
            // load the Match fields configuration
            for (final String matchField : matchFields.split(",")) {
                ofbMatchFields.add(OfbMatchFields.valueOf(matchField.trim().toUpperCase()));
            }
            setOfbMatchFields(ofbMatchFields);
            LOGGER.info(toString());
            LOGGER.info("The switch configuration is loaded.");
        } catch (final Exception e) {
            LOGGER.error("Error loading the configuration", e);
        }
    }

    private Site retrieveSite() {
        for(Site site : topology.getSites()) {
            if(site.getId().equals(serverName)) {
                return site;
            }
        }
        return null; // should never be null
    }


    @Override
    public byte[] getMacSwitch() {
        return macSwitch;
    }

    @Override
    public void setMacSwitch(byte[] macSwitch) {
        this.macSwitch = macSwitch == null ? null : macSwitch;
    }

    @Override
    public PSConnector getConnectorById(String id) {
        if (id == null) {
            return null;
        }
        if (localLanConnector != null && id.equals(localLanConnector.getId())) {
            return localLanConnector;
        }
        if (greConnectors != null) {
            return greConnectors.get(id);
        }
        return null;
    }

    @Override
    public PSConnector getConnectorByIp(String ip) {
        if (ip == null) {
            return null;
        }
        if (localLanConnector != null && ip.equalsIgnoreCase(localLanConnector.getNextHopIpAddress())) {
            return localLanConnector;
        }
        if (greConnectors != null) {
            for (final PSConnector connector : greConnectors.values()) {
                if (ip.equalsIgnoreCase(connector.getNextHopIpAddress())) {
                    return connector;
                }
            }
        }
        return null;
    }

    @Override
    public PSConnector getConnectorByGatewayIp(String ip) {
        if (ip == null) {
            return null;
        }
//        if (localLanConnector != null && ip.equalsIgnoreCase(localLanConnector.getGateway())) {
//            return localLanConnector;
//        }
        if (greConnectors != null) {
            for (final PSConnector connector : greConnectors.values()) {
//                if (ip.equalsIgnoreCase(connector.getGateway())) {
//                    return connector;
//                }
            }
        }
        return null;
    }

    @Override
    public PSConnector getLocalLanConnector() {
        return localLanConnector;
    }

    @Override
    public void setLocalLanConnector(PSConnector ingress) {
        this.localLanConnector = ingress;
    }

    @Override
    public Collection<PSConnector> getGreConnectors() {
        return greConnectors.values();
    }

    @Override
    public PSConnector connectorNetworkRange(String ip) {
        if (ip == null || greConnectors == null && localLanConnector == null) {
            return null;
        }
//        if (localLanConnector != null && localLanConnector.getSubnetInfo().isInRange(ip)) {
//            return localLanConnector;
//        }
        if (greConnectors != null) {
            for (final PSConnector cn : greConnectors.values()) {
//                if (cn.getSubnetInfo().isInRange(ip)) {
//                    return cn;
//                }
            }
        }
        return null;
    }

    @Override
    public boolean hasGreConnectors() {
        return greConnectors != null && greConnectors.size() > 0;
    }

    @Override
    public boolean addGreConnector(PSConnector egress) {
        if (this.greConnectors == null) {
            this.greConnectors = new HashMap<String, PSConnector>();
        }
        return this.greConnectors.put(egress.getId(), egress) == null;
    }

    @Override
    public boolean changeEgressConnectorById(String id, boolean active) {
        if (this.greConnectors == null) {
            return false;
        }
        final PSConnector connector = this.greConnectors.get(id);
        if (connector == null) {
            return false;
        }
        connector.setActive(active);
        return true;
    }

    @Override
    public String toString() {
        return String
                .format("SwitchConfigManagerImpl [localLanConnector=%s, greConnectors=%s, systemProperties=%s, macSwitch=%s]",
                        localLanConnector, greConnectors, systemProperties, NetUtils.bytesToHexString(macSwitch));
    }

    @Override
    public Set<OfbMatchFields> getOfbMatchFields() {
        return ofbMatchFields;
    }

    @Override
    public void setOfbMatchFields(Set<OfbMatchFields> ofbMatchFields) {
        this.ofbMatchFields = ofbMatchFields;
    }

    @Override
    public SystemProperties getSystemProperties() {
        return systemProperties;
    }

    @Override
    public void setSystemProperties(SystemProperties systemProperties) {
        this.systemProperties = systemProperties;
    }

    @Override
    public Topology getTopology() {
        return topology;
    }

    @Override
    public void setTopology(Topology topology) {
        this.topology = topology;
    }

    @Override
    public String getServerName() {
        return serverName;
    }

    @Override
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public Site getCurrentSite() {
        return currentSite;
    }

    @Override
    public String getConfigurationAsHtml() {
        final StringBuilder sb = new StringBuilder(500);
        sb.append("<pre>").append(this.toString()).append("</pre>");
        return sb.toString();
    }

}