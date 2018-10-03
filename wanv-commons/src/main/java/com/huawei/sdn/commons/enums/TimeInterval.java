package com.huawei.sdn.commons.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by root on 3/11/14.
 */
@XmlEnum
@XmlType(name="interval")
public enum TimeInterval {

    @XmlEnumValue("0")
    INTERVAL_ILLEGAL(0),
    @XmlEnumValue("3.33")
    INTERVAL_3_33_MS(1),
    @XmlEnumValue("10")
    INTERVAL_10_MS(2),
    @XmlEnumValue("100")
    INTERVAL_100_MS(3),
    @XmlEnumValue("1000")
    INTERVAL_1_S(4),
    @XmlEnumValue("10000")
    INTERVAL_10_S(5),
    @XmlEnumValue("60000")
    INTERVAL_1_M(6),
    @XmlEnumValue("600000")
    INTERVAL_10_M(7);

    private int period;

    TimeInterval(int period) {
        this.period = period;
    }

    public int getPeriod() {
        return period;
    }

}
