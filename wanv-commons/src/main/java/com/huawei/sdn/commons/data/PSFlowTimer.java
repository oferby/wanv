package com.huawei.sdn.commons.data;

import java.util.Calendar;

/**
 * Created by Ofer Ben-Yacov on 11/19/2014.
 */
public class PSFlowTimer {

    private PSFlow psFlow;
    private int elapseTime = 0;

    public PSFlow getPsFlow() {
        return psFlow;
    }

    public void setPsFlow(PSFlow psFlow) {
        this.psFlow = psFlow;
    }

    public int getElapseTime() {
        return elapseTime;
    }

    public void setElapseTime() {
        this.elapseTime = (int) (Calendar.getInstance().getTimeInMillis() - psFlow.getCreationTime()) / 1000;
    }
}
