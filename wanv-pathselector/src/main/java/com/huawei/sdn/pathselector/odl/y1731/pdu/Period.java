package com.huawei.sdn.pathselector.odl.y1731.pdu;

/**
 * Created by Ofer Ben-Yacov on 9/28/2014.
 */
public enum Period {

    INVALID((byte) 0, 0),
    FPS_300((byte) 1, 3),
    FPS_100((byte) 2, 10),
    FPS_10((byte) 3, 100),
    FPS_1((byte) 4, 1000),
    FPM_6((byte) 5, 600),
    FPM_1((byte) 6, 3600),
    FPH_6((byte) 7, 36000);

    private byte periodValue;
    private int delay;

    Period(byte periodValue, int delay) {
        this.periodValue = periodValue;
        this.delay = delay;
    }

    public byte getPeriodValue() {
        return periodValue;
    }

    public int getDelay() {
        return delay;
    }
}
