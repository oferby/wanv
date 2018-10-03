package com.huawei.sdn.pathselector.odl.y1731.pdu.dmm;

import com.huawei.sdn.pathselector.odl.y1731.pdu.AbstractOamPdu;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by Ofer Ben-Yacov on 10/6/2014.
 */
public class Dmr extends AbstractOamPdu {

    private static final int PDU_LENGTH = 37;

    private long txTimeStampF;
    private long rxTimeStampF;

    public Dmr() {

        this.version = 1;
        this.opCode = 46;
        this.tlvOffset = 32;

//        pro-active operation
        this.flags = 0b00000001;

    }

    public Dmr(byte[] payload) {

        this.version = 1;
        this.opCode = 46;
        this.tlvOffset = 32;

//        pro-active operation
        this.flags = 0b00000001;

        ByteBuffer bf = ByteBuffer.allocate(8);
        bf.put(Arrays.copyOfRange(payload, 4, 12));
        bf.position(0);
        txTimeStampF = bf.getLong();

        bf = ByteBuffer.allocate(8);
        bf.put(Arrays.copyOfRange(payload, 12, 20));
        bf.position(0);
        rxTimeStampF = bf.getLong();


    }

    public byte[] getPduFromDmm(byte[] dmmPdu) {

        ByteBuffer bf = ByteBuffer.allocate(PDU_LENGTH);
        super.addHeaders(bf);

//        txTimeStampF
        bf.put(Arrays.copyOfRange(dmmPdu, 4, 12));

//        rxTimeStampF
        bf.put(ByteBuffer.allocate(8).putLong(System.currentTimeMillis()).array());

//        txTimeStampB
        bf.put(ByteBuffer.allocate(8).putLong(0).array());

//        rxTimeStampB
        bf.put(ByteBuffer.allocate(8).putInt(0).array());

        return bf.array();

    }


}
