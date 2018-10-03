package com.huawei.sdn.pathselector.odl.y1731.pdu.ccm;

import org.opendaylight.controller.sal.packet.Ethernet;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ofer Ben-Yacov on 10/1/2014.
 */
public class CcmHandler {

    private String connectorId;
    private Ccm ccm;
    private Ethernet ethernet;
    private CcmStatus ccmStatus = new CcmStatus();


    private Timer loseOfConnectivityTimer;
    private LastCcmReceivedTask lastCcmReceivedTask = new LastCcmReceivedTask();
    private int delay;

    ConnectivityLossListener listener;

    public CcmHandler(String connectorId, Ccm ccm, Ethernet ethernet, ConnectivityLossListener listener) {
        this.connectorId = connectorId;
        this.ccm = ccm;
        this.ethernet = ethernet;
        this.delay = (int) (ccm.getPeriod().getDelay() * 3.5);
        this.listener = listener;
        loseOfConnectivityTimer = new Timer("CC-Loss-Timer-" + connectorId);
        this.resetLoseOfConnectivityTimer();
    }

    public String getConnectorId() {
        return connectorId;
    }

    public Ccm getCcm() {
        return ccm;
    }

    public void setCcm(Ccm ccm) {
        this.ccm = ccm;
    }

    public Ethernet getEthernet() {
        return ethernet;
    }

    public void setEthernet(Ethernet ethernet) {
        this.ethernet = ethernet;
    }

    public void resetCcmInEthernet() {
        ethernet.setRawPayload(ccm.getPayload());
    }

    public CcmStatus getCcmStatus() {
        return ccmStatus;
    }

    public void setLastMessageReceived() {
        this.ccmStatus.setLastMessageReceived(System.currentTimeMillis());
    }

    public synchronized void resetLoseOfConnectivityTimer() {

        try {
            lastCcmReceivedTask.cancel();
        } catch (Exception e) {
            e.printStackTrace();
//            could be that the task was already canceled or not started
        }
        lastCcmReceivedTask = new LastCcmReceivedTask();
        loseOfConnectivityTimer.schedule(lastCcmReceivedTask, delay);

    }

    @Override
    public String toString() {
        return "CcmHandler{" +
                "connectorId='" + connectorId + '\'' +
                ", ccmStatus=" + ccmStatus +
                ", delay=" + delay +
                '}';
    }

    class LastCcmReceivedTask extends TimerTask {

        @Override
        public void run() {

            CcmHandler.this.getCcmStatus().setConnected(false);
            listener.connectivityLost(connectorId);
        }
    }


}
