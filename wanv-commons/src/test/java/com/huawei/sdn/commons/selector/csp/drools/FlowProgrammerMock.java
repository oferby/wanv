package com.huawei.sdn.commons.selector.csp.drools;

import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.selector.flow.FlowProgrammer;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 8/11/14
 * Time: 9:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class FlowProgrammerMock implements FlowProgrammer{

    private FlowProgrammer flowProgrammer;


    @Override
    public void addFlow(PSFlow flow) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void modifyFlow(PSFlow flow) {
        System.out.println("Got new flow modification: " + flow.toString());
    }

    @Override
    public void removeFlow(PSFlow flow) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setFlowProgrammer(FlowProgrammer flowProgrammer) {
        this.flowProgrammer = flowProgrammer;
    }
}
