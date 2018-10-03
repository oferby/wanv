package com.huawei.sdn.pathselector.statistics;

import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.db.model.FlowEntity;

import java.util.Map;
import java.util.Set;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 3/31/2015.
 */
public interface FlowStatisticsController {

    void save(FlowEntity flowEntity);

    void save(PSFlow psFlow);

    Map<String,Map<String,Integer>> getNumberOfDestinationPortAndIpRequests();

    Map<String,Map<String, Set<String>>> getIpToIpAndPort();

}
