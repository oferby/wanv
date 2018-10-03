package com.huawei.sdn.pathselector.odl.y1731.pdu.dmm;

import com.huawei.sdn.commons.data.PSConnector;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * Created by Ofer Ben-Yacov on 10/6/2014.
 */
public class LinkMeasurement {

    private PSConnector greConnector;

    private DescriptiveStatistics frameDelayStatistics;

    public LinkMeasurement(PSConnector greConnector, int sampleSize) {
        this.greConnector = greConnector;
        this.frameDelayStatistics = new DescriptiveStatistics(sampleSize);
    }

    public PSConnector getGreConnector() {
        return greConnector;
    }

    public void addFrameDelay(long frameDelay){

        frameDelayStatistics.addValue(frameDelay);

    }

    public double getFrameDelayMean(){

        return frameDelayStatistics.getMean();

    }

    public double getFrameDelayStandardDeviation(){
        return frameDelayStatistics.getStandardDeviation();
    }


}
