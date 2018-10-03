package com.huawei.sdn.pathselector.odl.y1731.pdu;

import java.nio.ByteBuffer;

/**
 * Created by Ofer Ben-Yacov on 9/23/2014.
 */
public class AbstractOamPdu {


    public final static int Y1731_HEADER_SIZE_OCTETS = 4;
    public final static int ETHERNET_FRAME_SIZE_OCTETS = 14;

    protected Mel mel = Mel.LEVEL_7;
    protected byte version;
    protected byte opCode;
    protected byte flags;
    protected byte tlvOffset;
    protected byte endTlv = 0;

    public Mel getMel() {
        return mel;
    }

    public void setMel(Mel mel) {
        this.mel = mel;
    }


    public byte getMelAndVersionOctet() {

        return (byte) (0x000000FF & (version | (mel.getBytes() << 5)));

    }

    public byte getOpCode() {
        return opCode;
    }

    public byte getFlags() {
        return flags;
    }

    public byte getTlvOffset() {
        return tlvOffset;
    }

    public byte getEndTlv() {
        return endTlv;
    }

    protected void addHeaders(ByteBuffer bf){

        bf.put(this.getMelAndVersionOctet());
        bf.put(opCode);
        bf.put(flags);
        bf.put(tlvOffset);

    }

    protected void setHeadersFromPayload(byte[] payload){

        mel = Mel.values()[(0b11100000 & payload[0]) >> 5 ];
        version = (byte) (0b00011111 & payload[0]);
        opCode = payload[1];
        flags = payload[2];
        tlvOffset = payload[3];

    }
}