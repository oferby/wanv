package com.huawei.sdn.commons;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ofer Ben-Yacov on 11/19/2014.
 */
@RunWith(JUnit4.class)
public class SimpleTest {


    @Test
    public void testInt(){

        long i = 1000000;
        long j = 200000;

        int l = (int) ((i - j) / 2);

        System.out.println("l:" + l);

    }

    @Test
    public void testDate(){


        SimpleDateFormat dateFormat = new SimpleDateFormat();

        dateFormat.applyPattern("kk:mm:ss");

        System.out.println(dateFormat.format(new Date()));


    }


}
