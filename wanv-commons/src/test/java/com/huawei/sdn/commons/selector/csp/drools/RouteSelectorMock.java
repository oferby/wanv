package com.huawei.sdn.commons.selector.csp.drools;

import com.huawei.sdn.commons.enums.ConnectorType;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.selector.routing.message.RouteInfo;
import org.apache.commons.net.util.SubnetUtils;

import java.net.InetAddress;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 8/7/14
 * Time: 10:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class RouteSelectorMock implements RouteSelector {

    private List<PSConnector> psConnectors = new ArrayList<>();

    public RouteSelectorMock() {

        PSConnector connector = new PSConnector("1", ConnectorType.GRE);
        psConnectors.add(connector);

        connector = new PSConnector("2", ConnectorType.GRE);
        connector.setMovable(true);
        psConnectors.add(connector);

        connector = new PSConnector("3", ConnectorType.GRE);
        connector.setMovable(true);
        psConnectors.add(connector);

        connector = new PSConnector("4", ConnectorType.REMOTE);
        psConnectors.add(connector);

        connector = new PSConnector("5", ConnectorType.REMOTE);
        psConnectors.add(connector);

        connector = new PSConnector("6", ConnectorType.REMOTE);
        psConnectors.add(connector);

        connector = new PSConnector("7", ConnectorType.LOCAL);
        psConnectors.add(connector);
    }


    @Override
    public String getCidrForLocalAddress(String ipAddress) {
        return null;
    }

    @Override
    public PSConnector getConnector(String srcIpAddress, String dstIpAddress, boolean checkStatus) {

        List<PSConnector>tempList= new ArrayList<>();
        if(dstIpAddress.equals("GRE")){
            for (PSConnector psConnector : psConnectors) {
                if(psConnector.getType()==ConnectorType.GRE)
                    tempList.add(psConnector);
            }

        } else if (dstIpAddress.equals("LOCAL")){
            for (PSConnector psConnector : psConnectors) {
                if(psConnector.getType()==ConnectorType.LOCAL)
                    tempList.add(psConnector);
            }
        } else {
            for (PSConnector psConnector : psConnectors) {
                if(psConnector.getType()==ConnectorType.REMOTE)
                    tempList.add(psConnector);
            }

        }

        return tempList.get(0);
    }

    @Override
    public boolean isConnected(String ipAddress) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isSelfIpAddress(String ipAddress) {
        return false;
    }

    @Override
    public boolean isDefaultGateway(String ipAddress) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setDefaultRoutePortId(String ipAddress, int portId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public InetAddress getLocalIpForSubnet(InetAddress address) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<InetAddress> getLocalIpAddresses() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<String> getLocalSubnetSet() {
        return null;
    }

    @Override
    public Set<PSConnector> getAllConnectors() {

        return new HashSet<>(psConnectors);
    }

    @Override
    public PSConnector getConnector(String id) {
        for (PSConnector psConnector : getAllConnectors()) {
            if (psConnector.getId().equals(id))
                return psConnector;
        }

        return null;

    }

    @Override
    public void linkStatusChanged(String connectorId, boolean connected) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<PSConnector> getDefaultRoutes() {
        return null;
    }

    @Override
    public List<String> getDefaultRoutesIdList() {
        return null;
    }

    @Override
    public List<PSConnector> getTunnelConnectorList() {
        return null;
    }

    @Override
    public List<String> getRemoteSiteIdList() {
        return null;
    }

    @Override
    public Map<String, Set<String>> getConnectorIdPerTunnel() {
        return null;
    }

    @Override
    public RouteInfo getFullRouteInfo() {
        return null;
    }

    @Override
    public RouteInfo getRouteInfo() {
        return null;
    }
}
