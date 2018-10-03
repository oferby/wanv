package com.huawei.sdn.commons.db.model;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 3/10/2015.
 */
public class Tunnel {

    private int id;
    private String localIp;
    private String remoteIp;

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
}
