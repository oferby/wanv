package com.huawei.sdn.pathselector.odl.y1731.task;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ofer Ben-Yacov on 11/6/2014.
 */
public class TrafficRateStatistics {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrafficRateStatistics.class);

    private String linkId;
    private long lastSample;

    public TrafficRateStatistics(String linkId) {
        this.linkId = linkId;
        this.lastSample = 0;
    }

    public double addSampleAndCheckSdScore(long sample){

        long delta = sample - lastSample;
        lastSample = sample;

        double sdScore = delta - 10;

        LOGGER.debug("OAM: linkId: " + linkId + ", delta: " + delta );

        return sdScore;

    }

}
