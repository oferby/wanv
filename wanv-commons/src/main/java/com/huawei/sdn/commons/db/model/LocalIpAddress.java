package com.huawei.sdn.commons.db.model;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 3/10/2015.
 */
public class LocalIpAddress {


    private int id;
    private String ipAddress;
    private String subnet;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSubnet() {
        return subnet;
    }

    public void setSubnet(String subnet) {
        this.subnet = subnet;
    }
}
