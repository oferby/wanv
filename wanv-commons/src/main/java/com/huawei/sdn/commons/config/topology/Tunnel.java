package com.huawei.sdn.commons.config.topology;

import com.huawei.sdn.commons.config.topology.params.ParamXmlAdapter;
import com.huawei.sdn.commons.enums.TunnelParams;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Map;

/**
 * Created by root on 6/15/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Tunnel {

    @XmlAttribute
    private int id;
    @XmlAttribute(name = "local_ip")
    private String localIp;
    @XmlAttribute(name = "remote_ip")
    private String remoteIp;
    @XmlAttribute(name = "local_mac")
    private String localMac;
    @XmlAttribute(name = "remote_mac")
    private String remoteMac;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public String getLocalMac() {
        return localMac;
    }

    public void setLocalMac(String localMac) {
        this.localMac = localMac;
    }

    public String getRemoteMac() {
        return remoteMac;
    }

    public void setRemoteMac(String remoteMac) {
        this.remoteMac = remoteMac;
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }
}
