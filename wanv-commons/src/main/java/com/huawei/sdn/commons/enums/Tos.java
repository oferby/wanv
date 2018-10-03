package com.huawei.sdn.commons.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 * Created by root on 7/24/14.
 */
@XmlEnum
public enum Tos {


    @XmlEnumValue(value = "184")
    VOIP(184),
    @XmlEnumValue(value = "160")
    VIDEO(160),
    @XmlEnumValue(value = "64")
    SIGNALLING(64),
    @XmlEnumValue(value = "12")
    CRITICAL_DATA(12),
    @XmlEnumValue(value = "0")
    BEST_EFFORT(0);

    private int tos;

    private Tos(int tos) {
        this.tos = tos;
    }

    public int getTos() {
        return tos;
    }
}
