package com.huawei.sdn.commons.config.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 4/8/2015.
 */
@Component
public class OsNetworkHelperImpl implements OsNetworkHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(OsNetworkHelperImpl.class);


    @Override
    public byte[] getMacAddress(String localIpAddress) {

        try {

            NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getByName(localIpAddress));

            assert network != null;

            return network.getHardwareAddress();

        } catch (SocketException | UnknownHostException e) {
            LOGGER.error("Got Exception: ", e);
        }

        return null;

    }

    public List<String> getAllBridgeCidr(){

        List<String> cidrList = new ArrayList<>();

        try {

            NetworkInterface br1 = NetworkInterface.getByName("br1");

            for (InterfaceAddress interfaceAddress : br1.getInterfaceAddresses()) {

                cidrList.add(interfaceAddress.getAddress().getHostAddress() + "/" + interfaceAddress.getNetworkPrefixLength());

            }

        } catch (SocketException e) {
            LOGGER.error("", e);
        }

        return cidrList;
    }


}
