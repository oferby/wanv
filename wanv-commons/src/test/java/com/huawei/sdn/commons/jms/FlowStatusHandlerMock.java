package com.huawei.sdn.commons.jms;

import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.selector.flow.FlowStatusHandler;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/28/14
 * Time: 7:25 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class FlowStatusHandlerMock implements FlowStatusHandler {
    @Override
    public void linkStatusChanged(String connectorId, boolean connected) {
        assert connectorId!=null;
    }

//    @Override
//    public void flowRemoved(long flowId) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public void flowRemoved(String connectorId, long flowId) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }

    @Override
    public void flowRemoved(PSFlow psFlow) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
