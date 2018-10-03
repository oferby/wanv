package com.huawei.sdn.pathselector.odl.y1731;

import org.springframework.util.Assert;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by Ofer Ben-Yacov on 9/28/2014.
 */
public class TestPdu {


    public static void main(String[] args){

//        CcmHandler ccmHandler = new CcmHandler("1",new Ccm(Period.FPS_10, (short) 0, ""),null, null);
//
//        Assert.notNull(ccmHandler);
//
//        byte[] payload = ccmHandler.getCcm().getPayload();
//
//        Assert.notNull(payload);


        int f = 75;

        ByteBuffer byteBuffer = ByteBuffer.allocate(4).putInt(f);
        byte[] array = byteBuffer.array();





        ByteBuffer bf = ByteBuffer.allocate(4).put(Arrays.copyOfRange(array, 0, 4));
        bf.position(0);
        int txFcf = bf.getInt();

        Assert.isTrue(txFcf>0);

    }



}
