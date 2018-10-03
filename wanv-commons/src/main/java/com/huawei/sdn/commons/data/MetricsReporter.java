package com.huawei.sdn.commons.data;

import java.util.List;

/**
 * Created by root on 5/26/14.
 */
public interface MetricsReporter {
    public List<CurrentLinkMetrics> getCurrentMetrics();
}
