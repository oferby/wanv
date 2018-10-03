package com.huawei.sdn.commons.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 * Created by root on 7/9/14.
 */
@XmlEnum
public enum TunnelParams {
    @XmlEnumValue("performance_metrics_port")
    METRICS_PORT("performance_metrics_port");

    private String alias;

    private TunnelParams(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }
}
