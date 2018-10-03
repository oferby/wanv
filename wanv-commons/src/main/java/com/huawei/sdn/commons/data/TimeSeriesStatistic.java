package com.huawei.sdn.commons.data;

import java.util.Date;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 3/25/2015.
 */
public class TimeSeriesStatistic {

    private Date date;
    private double value;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
