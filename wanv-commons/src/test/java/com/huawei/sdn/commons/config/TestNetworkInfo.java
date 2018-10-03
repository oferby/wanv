package com.huawei.sdn.commons.config;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 4/8/2015.
 */
public class TestNetworkInfo {


    @Test
    public void getMac() {

        String connectorId = "2";

        try (BufferedReader br = new BufferedReader(new FileReader("D:\\Temp\\dmp.txt"))) {

            String line = br.readLine();

            while (line != null) {

                if (line.startsWith(" " + connectorId)) {
                    System.out.println(line);

                    System.out.println(line.substring(line.indexOf("addr") + 5, line.length()));

                }

                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
