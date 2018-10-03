package com.huawei.sdn.commons.selector.routing.selector;

import com.huawei.sdn.commons.config.system.OsNetworkHelper;
import com.huawei.sdn.commons.tools.NetUtils;

import java.net.InetAddress;
import java.util.List;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 4/21/2015.
 */
public class OsNetworkHelperMock implements OsNetworkHelper{
    @Override
    public byte[] getMacAddress(String localIpAddress) {
        return NetUtils.hexStringToByteArray("01:00:00:00:00:1");
    }

    @Override
    public List<String> getAllBridgeCidr() {
        return null;
    }

}
