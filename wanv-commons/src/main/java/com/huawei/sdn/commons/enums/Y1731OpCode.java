package com.huawei.sdn.commons.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 3/23/14.
 */
@XmlType(name = "operation")
@XmlEnum
public enum Y1731OpCode {

    @XmlEnumValue("heartbeat")
    CCM(1, "heartbeat"),
    @XmlEnumValue("frame-delay")
    DMM(47, "frame-delay"),
    @XmlEnumValue("frame-delay-response")
    DMR(46, "frame-delay-response");

    private int value;
    private String alias;
    private static Map<Integer, Y1731OpCode> valueToEnum = new HashMap<Integer, Y1731OpCode>();
    private static Map<String, Y1731OpCode> nameToEnum = new HashMap<String, Y1731OpCode>();
    static {
        for(Y1731OpCode y1731OpCode : Y1731OpCode.values()) {
            valueToEnum.put(y1731OpCode.getValue(), y1731OpCode);
            nameToEnum.put(y1731OpCode.getAlias(), y1731OpCode);
        }
    }

    private Y1731OpCode(int value, String alias) {
        this.value = value;
        this.alias = alias;
    }

    public int getValue() {
        return value;
    }

    public String getAlias() {
        return alias;
    }

    public static Y1731OpCode getOpCode(int value) {
        return valueToEnum.get(value);
    }

    public static Y1731OpCode fromValue(String value) {
        return nameToEnum.get(value);
    }


}
