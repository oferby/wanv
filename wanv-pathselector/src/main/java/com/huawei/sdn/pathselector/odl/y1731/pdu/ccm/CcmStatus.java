package com.huawei.sdn.pathselector.odl.y1731.pdu.ccm;

import com.huawei.sdn.pathselector.odl.y1731.pdu.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Ofer Ben-Yacov on 10/2/2014.
 */
public class CcmStatus {

    private static final Logger LOGGER = LoggerFactory.getLogger(CcmStatus.class);

    private boolean connected;
    private long lastMessageReceived;
    private long lastLossCalculation = System.currentTimeMillis();
    private AtomicLong txFc1 = new AtomicLong(0);
    private AtomicLong rxFc1 = new AtomicLong(0);

    private AtomicLong previousTxFcb = new AtomicLong(0);
    private AtomicLong previousTxFcf = new AtomicLong(0);
    private AtomicLong previousRxFcb = new AtomicLong(0);
    private AtomicLong previousRxFc1 = new AtomicLong(0);

    private AtomicLong farEndFrameLoss = new AtomicLong(0);
    private AtomicLong nearEndFrameLoss = new AtomicLong(0);

    private int farEndAccumulatedFrameLoss = 0;
    private int nearEndAccumulatedFrameLoss = 0;

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean setConnectedAndAlertToggle(boolean connected){

        boolean toggled = false;
        if(this.connected != connected) {
            toggled = true;
            this.connected = connected;
        }

        return toggled;

    }

    public long increaseTxFc1() {
        return this.txFc1.incrementAndGet();
    }

    public long increaseRxFc1() {
        return this.rxFc1.incrementAndGet();
    }

    public void setLastMessageReceived(long lastMessageReceived) {
        this.lastMessageReceived = lastMessageReceived;
    }

    public void calcFrameLoss(int txFcb, int rxFcb, int txFcf){
        int currentRxFc1 = (int) rxFc1.get();
        int feFrameLoss = (int) (Math.abs(txFcb - previousTxFcb.get()) - Math.abs(rxFcb - previousRxFcb.get()));
        farEndFrameLoss.set(feFrameLoss);
        farEndAccumulatedFrameLoss+=feFrameLoss;

        int neFrameLoss= (int) (Math.abs(txFcf - previousTxFcf.get()) - Math.abs(currentRxFc1 - previousRxFc1.get()));
        nearEndFrameLoss.set(neFrameLoss);
        nearEndAccumulatedFrameLoss+=neFrameLoss;


//       LOGGER.debug("ETH-LM: txFcb:" + txFcb
//       + ", rxFcb:" + rxFcb
//       + ", txFcf:" + txFcf
//       + ", rxFc1:" + currentRxFc1
//       + ", farEndFrameLoss:" + feFrameLoss
//       + ", nearEndFrameLoss:" + neFrameLoss
//       + ", farEndAccumulatedFrameLoss:" + farEndAccumulatedFrameLoss
//       + ", nearEndAccumulatedFrameLoss:" +nearEndAccumulatedFrameLoss);

        previousRxFcb.set(rxFcb);
        previousTxFcb.set(txFcb);
        previousTxFcf.set(txFcf);
        previousRxFc1.set(currentRxFc1);

    }

    public long getFarEndFrameLoss() {
        return farEndFrameLoss.get();
    }

    public long getNearEndFrameLoss(){
        return nearEndFrameLoss.get();
    }

    public float getAndResetFrameLossRate(Period period){

        long currentTime = System.currentTimeMillis();
        long timePassed =  currentTime - lastLossCalculation;
        lastLossCalculation = currentTime;

        long totalFrameLost = nearEndAccumulatedFrameLoss + farEndAccumulatedFrameLoss;

        if(totalFrameLost==0)
            return 0;

        nearEndAccumulatedFrameLoss = 0;
        farEndAccumulatedFrameLoss = 0;

        float frameRate = timePassed * 2 / period.getDelay() ;

        float frameLossRate = totalFrameLost / frameRate * 100;

        LOGGER.debug("ETH-LM: frameLossRate:" + frameLossRate);

        return frameLossRate;

    }

    @Override
    public String toString() {
        return "CcmStatus{" +
                "connected=" + connected +
                ", lastMessageReceived=" + lastMessageReceived +
                ", lastLossCalculation=" + lastLossCalculation +
                ", txFc1=" + txFc1 +
                ", rxFc1=" + rxFc1 +
                ", previousTxFcb=" + previousTxFcb +
                ", previousTxFcf=" + previousTxFcf +
                ", previousRxFcb=" + previousRxFcb +
                ", previousRxFc1=" + previousRxFc1 +
                ", farEndFrameLoss=" + farEndFrameLoss +
                ", nearEndFrameLoss=" + nearEndFrameLoss +
                ", farEndAccumulatedFrameLoss=" + farEndAccumulatedFrameLoss +
                ", nearEndAccumulatedFrameLoss=" + nearEndAccumulatedFrameLoss +
                '}';
    }
}
