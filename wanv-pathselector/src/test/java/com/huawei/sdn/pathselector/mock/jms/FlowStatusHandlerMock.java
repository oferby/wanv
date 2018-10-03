package com.huawei.sdn.pathselector.mock.jms;

import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.selector.flow.FlowStatusHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/28/14
 * Time: 9:20 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class FlowStatusHandlerMock implements FlowStatusHandler {

    private AtomicInteger messageReceived = new AtomicInteger();

    @Override
    public void linkStatusChanged(String connectorId, boolean connected) {
//        try {
//            Thread.sleep(1000);
//        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println(System.currentTimeMillis() +  " FlowStatusHandler mock received message!");
        messageReceived.incrementAndGet();
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

    }

    public int getNumOfMessagesReceived() {
        return messageReceived.get();
    }
}
