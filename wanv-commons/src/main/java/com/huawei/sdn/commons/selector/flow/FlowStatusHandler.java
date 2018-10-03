package com.huawei.sdn.commons.selector.flow;


import com.huawei.sdn.commons.data.PSFlow;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/20/14
 * Time: 1:43 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FlowStatusHandler {

    void linkStatusChanged(String connectorId, boolean connected);

    void flowRemoved(PSFlow psFlow);


}
