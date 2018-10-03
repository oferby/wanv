package com.huawei.sdn.commons.config.topology;

import javax.xml.bind.annotation.*;

/**
 * Created by root on 6/16/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class Queue {

    @XmlAttribute
    private int id;
    @XmlElementWrapper(name = "tos_list")
    @XmlElement(name = "tos")
    private String[] tosList;
    @XmlAttribute
    private String priority;
    @XmlAttribute(name = "min_rate")
    private String minRate;
    @XmlAttribute(name = "max_rate")
    private String maxRate;
    @XmlAttribute
    private int minSec = 0;
    @XmlAttribute
    private int maxSec = Integer.MAX_VALUE;
    @XmlAttribute(name = "default")
    private boolean defaultQueue = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String[] getTosList() {
        return tosList;
    }

    public void setTosList(String[] tos) {

        if(tos==null)
            return;

        this.tosList = new String[tos.length];

        System.arraycopy(tos,0,tosList,0,tos.length);

    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getMinRate() {
        return minRate;
    }

    public void setMinRate(String minRate) {
        this.minRate = minRate;
    }

    public String getMaxRate() {
        return maxRate;
    }

    public void setMaxRate(String maxRate) {
        this.maxRate = maxRate;
    }

    public int getMinSec() {
        return minSec;
    }

    public int getMaxSec() {
        return maxSec;
    }

    public boolean isDefaultQueue() {
        return defaultQueue;
    }
}
