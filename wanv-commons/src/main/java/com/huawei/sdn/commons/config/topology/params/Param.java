package com.huawei.sdn.commons.config.topology.params;

import com.huawei.sdn.commons.enums.TunnelParams;

import javax.xml.bind.annotation.*;

/**
 * Created by Amir Kost on 7/9/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Param {

    @XmlAttribute
    private TunnelParams name;
    @XmlValue
    private String value;

    public Param() {
    }

    public TunnelParams getName() {
        return name;
    }

    public void setName(TunnelParams name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
