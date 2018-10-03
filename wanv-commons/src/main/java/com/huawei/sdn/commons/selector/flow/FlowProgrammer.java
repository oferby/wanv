package com.huawei.sdn.commons.selector.flow;

import com.huawei.sdn.commons.data.PSFlow;

import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 8/5/14
 * Time: 4:32 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FlowProgrammer {

    void addFlow(PSFlow flow);

    void modifyFlow(PSFlow flow);

    void removeFlow(PSFlow flow);

}
