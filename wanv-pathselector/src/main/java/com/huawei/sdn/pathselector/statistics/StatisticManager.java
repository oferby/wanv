package com.huawei.sdn.pathselector.statistics;

import com.huawei.sdn.commons.data.PSConnector;
import org.opendaylight.controller.sal.reader.NodeConnectorStatistics;

import java.util.List;
import java.util.Map;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 3/24/2015.
 */
public interface StatisticManager {

    List<PSConnector>getConnectorStatistics();
    List<NodeConnectorStatistics> getAllConnectorStatistics();
    List<Map<String,String>> getSiteTunnelStatistics(String siteId);
}
