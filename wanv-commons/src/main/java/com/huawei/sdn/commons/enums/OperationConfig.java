package com.huawei.sdn.commons.enums;

import com.huawei.sdn.commons.config.topology.Operation;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 * Created by root on 4/13/14.
 */

//@XmlType(name = "operation")
@XmlEnum
public enum OperationConfig {

    @XmlEnumValue("heartbeat")
    HEARTBEAT("heartbeat", new Y1731OpCode[]{Y1731OpCode.CCM}),
    @XmlEnumValue("frame-delay")
    FRAME_DELAY("frame-delay", new Y1731OpCode[]{Y1731OpCode.DMM, Y1731OpCode.DMR});

    private Y1731OpCode[] _operations;
    private String alias;

    private OperationConfig(String alias, Y1731OpCode[] operations) {

        this.alias = alias;

        if(operations==null) {
            return;
        }

        _operations = new Y1731OpCode[operations.length];

        System.arraycopy(operations,0,_operations,0,operations.length);

    }

    public Y1731OpCode[] getOperations() {
        return _operations;
    }

    public String getAlias() {
        return alias;
    }

}
