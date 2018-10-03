package com.huawei.sdn.commons.tools;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Amir Kost on 7/22/14.
 */
public class NumberParserTest {

    @Test
    public void testNumberParser() {
        String expression1 = "100-102,107,109-111";
        int[] numbers = NumberParser.parseNumbers(expression1);
        assertTrue(numbers.length == 7);
        assertArrayEquals(new int[] {100, 101, 102, 107, 109, 110, 111}, numbers);

        String expression2 = "300";
        numbers = NumberParser.parseNumbers(expression2);
        assertTrue(numbers.length == 1);
        assertArrayEquals(new int[]{300}, numbers);

        String expression3 = "100-102";
        numbers = NumberParser.parseNumbers(expression3);
        assertTrue(numbers.length == 3);
        assertArrayEquals(new int[]{100, 101, 102}, numbers);

    }


    @Test
    public void testMAcAddress(){

        String mac = "c2:e0:02:9f:36:42";

        byte[] bytes = NetUtils.hexStringToByteArray(mac);

        assert bytes!=null;

        String bytesToHexString = NetUtils.bytesToHexString(bytes);
        
        assert mac.equals(bytesToHexString);

    }
}
