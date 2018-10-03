package com.huawei.sdn.commons.db.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 3/31/2015.
 */
public class FlowEntity {

    private int id;
    private String timeStamp;
//    private String sourceMac;
//    private String destinationMac;
    private String sourceIp;
    private String destinationIp;
    private String sourcePort;
    private String destinationPort;
    private long tx;
    private long rx;

    public FlowEntity() {
    }

    public FlowEntity(Date timeStamp,  String sourceIp, String destinationIp, String sourcePort, String destinationPort) {
//        String sourceMac, String destinationMac,
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("dd/MM/yyyy kk:mm:ss");

        this.timeStamp = dateFormat.format(timeStamp);
//        this.sourceMac = sourceMac;
//        this.destinationMac = destinationMac;
        this.sourceIp = sourceIp;
        this.destinationIp = destinationIp;
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
    }

    public int getId() {
        return id;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

//    public String getSourceMac() {
//        return sourceMac;
//    }
//
//    public void setSourceMac(String sourceMac) {
//        this.sourceMac = sourceMac;
//    }
//
//    public String getDestinationMac() {
//        return destinationMac;
//    }
//
//    public void setDestinationMac(String destinationMac) {
//        this.destinationMac = destinationMac;
//    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getDestinationIp() {
        return destinationIp;
    }

    public void setDestinationIp(String destinationIp) {
        this.destinationIp = destinationIp;
    }

    public String getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(String sourcePort) {
        this.sourcePort = sourcePort;
    }

    public String getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(String destinationPort) {
        this.destinationPort = destinationPort;
    }

    public long getTx() {
        return tx;
    }

    public void setTx(long tx) {
        this.tx = tx;
    }

    public long getRx() {
        return rx;
    }

    public void setRx(long rx) {
        this.rx = rx;
    }



}
