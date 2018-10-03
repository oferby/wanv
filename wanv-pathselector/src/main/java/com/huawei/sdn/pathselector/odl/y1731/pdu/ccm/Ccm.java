package com.huawei.sdn.pathselector.odl.y1731.pdu.ccm;

import com.huawei.sdn.pathselector.odl.y1731.pdu.AbstractOamPdu;
import com.huawei.sdn.pathselector.odl.y1731.pdu.Period;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Ofer Ben-Yacov on 9/23/2014.
 */
public class Ccm extends AbstractOamPdu {

    public final static int Y1731_CCM_SIZE_OCTETS = 76;

    private AtomicInteger sequenceNumber = new AtomicInteger(0);
    private byte[] mepId;
    private byte[] megId;
    private AtomicLong txFcf = new AtomicLong(0);
    private AtomicLong rxFcb = new AtomicLong(0);
    private AtomicLong txFcb = new AtomicLong(0);


    public Ccm(Period period, short mepId, String megId) {
        version = 0;
        opCode = 1;
        flags|=period.getPeriodValue();
        tlvOffset=70;

        this.mepId = ByteBuffer.allocate(2).putShort((short) (0x1FFF & mepId)).array();
//        this.megId = this.getMegIdAsByteArray(megId);
        this.megId = ByteBuffer.allocate(48).putInt(0).array();
    }

    private Ccm(){

    }

    public Period getPeriod(){

        return Period.values()[(0b00000111 & flags)];

    }

    public void increaseSequence(){
        sequenceNumber.incrementAndGet();
    }

    public void setTxFcf(long txFc1){
        txFcf.set(txFc1);
    }

    public void setRxFcb(long rxFc1){
        rxFcb.set(rxFc1);
    }

    public boolean defectDetected(){
        return (flags >> 7) == 1;
    }

    public void setRdi(boolean rdi){
        flags|=0b10000000;
    }

    public void setTxFcb(int txFcb) {
        this.txFcb.set(txFcb);
    }

    public long getTxFcf() {
        return txFcf.get();
    }

    public long getRxFcb() {
        return rxFcb.get();
    }

    public long getTxFcb() {
        return txFcb.get();
    }

    private byte[] getMegIdAsByteArray(String megId){

            int len = megId.length();

            byte[] data = new byte[48];
            for (int i = (48 - len); i < 48; i += 2) {
                data[i / 2] = (byte) ((Character.digit(megId.charAt(i), 16) << 4)
                        + Character.digit(megId.charAt(i+1), 16));
            }
            return data;

    }

    public byte[] getPayload(){

        ByteBuffer bf = ByteBuffer.allocate(Y1731_CCM_SIZE_OCTETS);
        super.addHeaders(bf);

        bf.put(ByteBuffer.allocate(4).putInt(sequenceNumber.get()).array());
        bf.put(mepId);
//        bf.put(ByteBuffer.allocate(2).putInt(0).array());
        bf.put(megId);
//        bf.put(ByteBuffer.allocate(48).putInt(0).array());
        bf.put(ByteBuffer.allocate(4).putInt((int) txFcf.get()).array());
        bf.put(ByteBuffer.allocate(4).putInt((int) rxFcb.get()).array());
        bf.put(ByteBuffer.allocate(4).putInt((int) txFcb.get()).array());

//        reserved
        bf.put(ByteBuffer.allocate(4).putInt(0).array());

        bf.put(super.endTlv);

        return bf.array();

    }

    public static Ccm getFromPayload(byte[] payload){

        Ccm ccm = new Ccm();
        ccm.setHeadersFromPayload(payload);
        ccm.sequenceNumber.set(ByteBuffer.allocate(4).put(Arrays.copyOfRange(payload,5,9)).getInt());
        ccm.mepId = Arrays.copyOfRange(payload,9,11);
        ccm.megId = Arrays.copyOfRange(payload,11,59);
        ccm.txFcf.set(ByteBuffer.allocate(4).put(Arrays.copyOfRange(payload,59,63)).getInt());
        ccm.rxFcb.set(ByteBuffer.allocate(4).put(Arrays.copyOfRange(payload,63,67)).getInt());
        ccm.txFcb.set(ByteBuffer.allocate(4).put(Arrays.copyOfRange(payload,67,71)).getInt());

        return ccm;
    }

    @Override
    public String toString() {
        return "Ccm{" +
                "txFcf=" + txFcf.get() +
                ", rxFcb=" + rxFcb.get() +
                ", txFcb=" + txFcb.get() +
                '}';
    }
}
