package com.huawei.sdn.commons.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * The Connector Types
 */
@XmlType(name = "connectorType")
@XmlEnum
public enum ConnectorType {

    GRE, REMOTE, LOCAL, DROP;

    public String value() {
        return name();
    }

    public static ConnectorType fromValue(String v) {
        return valueOf(v);
    }

}
