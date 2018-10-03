package com.huawei.sdn.commons.config.topology;

import javax.xml.bind.annotation.*;

/**
 * Created by root on 6/15/14.
 */
@XmlRootElement(name = "ip_address")
@XmlAccessorType(XmlAccessType.FIELD)

public class IpAddress {

    @XmlAttribute
    private int id;
    @XmlValue
    private String ip;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
