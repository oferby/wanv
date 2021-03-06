package com.huawei.sdn.commons.config.system;

import java.net.InetAddress;
import java.util.List;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 4/21/2015.
 */
public interface OsNetworkHelper {
    byte[] getMacAddress(String localIpAddress);
    List<String> getAllBridgeCidr();
}
