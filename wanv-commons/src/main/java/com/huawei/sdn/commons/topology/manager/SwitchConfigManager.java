package com.huawei.sdn.commons.topology.manager;

import java.util.Collection;
import java.util.Set;

import com.huawei.sdn.commons.jms.properties.SystemProperties;
import com.huawei.sdn.commons.config.topology.Site;
import com.huawei.sdn.commons.config.topology.Topology;
import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.data.PSPacketIn.OfbMatchFields;

/**
 * Interface implemented by the switch configuration manager that includes all
 * the configuration and update method.<br/>
 * Only manage one switch configuration.
 */
public interface SwitchConfigManager {

    /**
     * Clear/initialize the configuration
     */
    void clear();

    /**
     * Load the configuration from config.xml file<br/>
     * the configuration file config.xml must be in the configuration
     * OpenDayLight folder
     */
    void loadConfiguration(String topologyFileName, String systemPropertiesFileName);

    /**
     * Get The physical switch address
     *
     * @return the physical address
     */
    byte[] getMacSwitch();

    /**
     * Change the physical Switch address
     *
     * @param macSwitch
     *            the new physical switch address
     */
    void setMacSwitch(byte[] macSwitch);

    /**
     * Retrieve a connector port with its id.
     *
     * @param id
     *            the connector id
     * @return the Connector Object or null if not found
     */
    PSConnector getConnectorById(String id);

    /**
     * Retrieve a connector port with its IP.
     *
     * @param ip
     *            the connector IP
     * @return the Connector Object or null if not found
     */
    PSConnector getConnectorByIp(String ip);

    /**
     * Retrieve a connector port with its gateway IP.
     *
     * @param ip
     *            the connector gateway IP
     * @return the Connector Object or null if not found
     */
    PSConnector getConnectorByGatewayIp(String ip);

    /**
     * Return The ingress Connector
     *
     * @return The ingress Connector
     */
    PSConnector getLocalLanConnector();

    /**
     * set the ingress connector
     *
     * @param ingress
     *            the ingress connector
     */
    void setLocalLanConnector(PSConnector ingress);

    /**
     * Return Egress ports Collection
     *
     * @return Collection of the Egress Connectors
     */
    Collection<PSConnector> getGreConnectors();

    /**
     * Try to find a connector (REMOTE or GRE) that the ip is in the
     * connector local network range
     *
     * @param ip
     *            the IP to check if is in the local network range
     * @return null if not in a port range, else the connector port
     */
    PSConnector connectorNetworkRange(String ip);

    /**
     * check if the switch has one ore more Egress ports
     *
     * @return true if the switch has at least 1 Egress port
     */
    boolean hasGreConnectors();

    /**
     * Add a egress connector
     *
     * @param egress
     *            the egress Connector to add
     * @return true if added, false if already exist
     */
    boolean addGreConnector(PSConnector egress);

    /**
     * Change a Connector active status.
     *
     * @param id
     *            The connector port id to change
     * @param active
     *            the new status
     * @return true if the connector exists else false
     */
    boolean changeEgressConnectorById(String id, boolean active);

    /**
     * Get the Match Fields Parameters to create the Match Flow clause
     *
     * @return the Match Fields Parameters
     */
    Set<OfbMatchFields> getOfbMatchFields();

    /**
     * change the Match Fields Parameters to create the Match Flow clause
     *
     * @param ofbMatchFields
     *            the new Match Fields Parameters
     */
    void setOfbMatchFields(Set<OfbMatchFields> ofbMatchFields);

    SystemProperties getSystemProperties();

    void setSystemProperties(SystemProperties systemProperties);

    Topology getTopology();

    void setTopology(Topology topology);

    String getServerName();

    void setServerName(String serverName);

    Site getCurrentSite();

    /**
     * Return the whole configuration as a simple HTML.
     *
     * @return the HTML configuration view.
     */
    String getConfigurationAsHtml();
}