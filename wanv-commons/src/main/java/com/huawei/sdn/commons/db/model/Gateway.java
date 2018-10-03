package com.huawei.sdn.commons.db.model;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 3/10/2015.
 */
public class Gateway {

    private int id;
    private String ip_address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }
}
