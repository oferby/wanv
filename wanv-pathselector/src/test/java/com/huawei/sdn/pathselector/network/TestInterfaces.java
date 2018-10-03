package com.huawei.sdn.pathselector.network;

import org.junit.Test;

import java.net.*;
import java.util.Enumeration;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 3/11/2015.
 */

public class TestInterfaces {


    @Test
    public void testInterfaces() throws SocketException {

        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

        while (networkInterfaces.hasMoreElements()){

            NetworkInterface networkInterface = networkInterfaces.nextElement();

            System.out.println(networkInterface.toString());

            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {

                if( interfaceAddress.getAddress() instanceof Inet4Address){

                    System.out.println("address: " + interfaceAddress.getAddress().getHostAddress());
                    System.out.println("subnet: " + interfaceAddress.getNetworkPrefixLength());

                }


            }


        }


    }


}
