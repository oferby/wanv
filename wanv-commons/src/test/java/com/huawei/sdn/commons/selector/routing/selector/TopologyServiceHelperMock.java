package com.huawei.sdn.commons.selector.routing.selector;

import com.huawei.sdn.commons.config.topology.TopologyServiceHelper;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 4/21/2015.
 */
public class TopologyServiceHelperMock implements TopologyServiceHelper {
    @Override
    public String getNodeId() {
        return "site2";
    }

    @Override
    public byte[] getNodeMacAddress() {
        return new byte[0];
    }
}
