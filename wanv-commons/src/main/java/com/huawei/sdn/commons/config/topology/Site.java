package com.huawei.sdn.commons.config.topology;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Site {

    @XmlAttribute
    private String id;
    @XmlElementWrapper(name = "ip_addresses")
    @XmlElement(name = "ip_address")
    private IpAddress[] _ipAddresses;
    @XmlElementWrapper(name = "tunnels")
    @XmlElement(name = "tunnel")
    private Tunnel[] _tunnels;
    @XmlElementWrapper(name = "routes")
    @XmlElement(name = "route")
    private Route[] _routes;
    @XmlElementWrapper(name = "queues")
    @XmlElement(name = "queue")
    private Queue[] _queues;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public IpAddress[] getIpAddresses() {
        return _ipAddresses;
    }

    public void setIpAddresses(IpAddress[] ipAddresses) {

        if(ipAddresses==null) {
            return;
        }

        this._ipAddresses = new IpAddress[ipAddresses.length];

        System.arraycopy(ipAddresses,0,_ipAddresses,0,ipAddresses.length);

    }

    public Tunnel[] getTunnels() {
        return _tunnels;
    }

    public void setTunnels(Tunnel[] tunnels) {

        if(tunnels==null) {
            return;
        }

        this._tunnels = new Tunnel[tunnels.length];

        System.arraycopy(tunnels,0,_tunnels,0,tunnels.length);

    }

    public Route[] getRoutes() {
        return _routes;
    }

    public void setRoutes(Route[] routes) {

        if(routes==null) {
            return;
        }

        this._routes = new Route[routes.length];

        System.arraycopy(routes,0,_routes,0,routes.length);

    }

    public Queue[] getQueues() {
        return _queues;
    }

    public void setQueues(Queue[] queues) {

        if(queues==null) {
            return;
        }

        this._queues = new Queue[queues.length];

        System.arraycopy(queues,0,_queues,0,queues.length);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Site)) {
            return false;
        }

        final Site site = (Site) o;

        return !(id != null ? !id.equals(site.id) : site.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
