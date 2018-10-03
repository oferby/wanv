package com.huawei.sdn.pathselector.odl.y1731.pdu;

/**
 * Created by Ofer Ben-Yacov on 9/23/2014.
 */
public enum Mel {

    LEVEL_0,
    LEVEL_1,
    LEVEL_2,
    LEVEL_3,
    LEVEL_4,
    LEVEL_5,
    LEVEL_6,
    LEVEL_7;

    private byte melBytes;

    public byte getBytes(){
        return (byte) this.ordinal();
    }

}
