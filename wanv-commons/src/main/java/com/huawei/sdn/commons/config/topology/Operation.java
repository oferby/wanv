package com.huawei.sdn.commons.config.topology;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.huawei.sdn.commons.enums.OperationConfig;
import com.huawei.sdn.commons.enums.TimeInterval;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Operation {

    @XmlAttribute(name = "name")
    private OperationConfig operationConfig;
    @XmlAttribute
    private TimeInterval interval;
    @XmlAttribute
    private boolean isEnabled;
    @XmlAttribute
    private int sampleSize = 10;

    public OperationConfig getOperationConfig() {
        return operationConfig;
    }

    public void setOperationConfig(OperationConfig opCodes) {
        this.operationConfig = opCodes;
    }

    public TimeInterval getInterval() {
        return interval;
    }

    public void setInterval(TimeInterval interval) {
        this.interval = interval;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public int getSampleSize() {
        return sampleSize;
    }
}
