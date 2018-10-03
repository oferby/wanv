package com.huawei.sdn.pathselector.odl.y1731.pdu.dmm;

import com.huawei.sdn.pathselector.odl.y1731.pdu.AbstractOamPdu;

import java.nio.ByteBuffer;

/**
 * Created by Ofer Ben-Yacov on 10/6/2014.
 */
public class Dmm extends AbstractOamPdu {

    private static final int PDU_LENGTH = 37;

    public Dmm() {
        this.version = 1;
        this.opCode = 47;
        this.tlvOffset = 32;

//        pro-active operation
        this.flags = 0b00000001;
    }

    public byte[] createPdu(){

        ByteBuffer bf = ByteBuffer.allocate(PDU_LENGTH);
        super.addHeaders(bf);

//      txTimeStampF
        bf.put(ByteBuffer.allocate(8).putLong(System.currentTimeMillis()).array());

//        reserved
        bf.put(ByteBuffer.allocate(24).putInt(0).array());

        bf.put(endTlv);

        return bf.array();

    }


}
