package com.huawei.sdn.commons.db.model;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 3/10/2015.
 */
public class StaticRoute {

    private int id;
    private String destIp;
    private String destSubnet;
    private String nextHop;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDestIp() {
        return destIp;
    }

    public void setDestIp(String destIp) {
        this.destIp = destIp;
    }

    public String getDestSubnet() {
        return destSubnet;
    }

    public void setDestSubnet(String destSubnet) {
        this.destSubnet = destSubnet;
    }

    public String getNextHop() {
        return nextHop;
    }

    public void setNextHop(String nextHop) {
        this.nextHop = nextHop;
    }
}
