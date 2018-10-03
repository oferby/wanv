package com.huawei.sdn.commons.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 5/14/14.
 */
@XmlType(name = "headers")
@XmlEnum
public enum HeaderType {

    @XmlEnumValue("Y1731")
    Y1731,
    @XmlEnumValue("VxLAN")
    VXLAN;

    private static Map<String, HeaderType> headerTypeMap = new HashMap<String, HeaderType>();
    static {
        for(HeaderType headerType : HeaderType.values()) {
            headerTypeMap.put(headerType.name(), headerType);
        }
    }

    public static HeaderType getByName(String value) {
        return headerTypeMap.get(value);
    }

}
