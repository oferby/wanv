package com.huawei.sdn.commons.config.topology;

import javax.xml.bind.annotation.*;

/**
 * Created by root on 6/15/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Route {

    @XmlAttribute(name = "id")
    private int id;
    @XmlAttribute(name = "gw")
    private String gateway;
    @XmlAttribute(name = "des")
    private String destination;
    @XmlAttribute(name = "max_rate")
    private int maxRate;
    @XmlAttribute(name = "publish")
    private boolean publish;

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getMaxRate() {
        return maxRate;
    }

    public void setMaxRate(int maxRate) {
        this.maxRate = maxRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPublish() {
        return publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }
}
