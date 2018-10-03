package com.huawei.sdn.pathselector.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by root on 8/4/14.
 */
@Component
public class TestConsumer implements MessageListener {

    private AtomicInteger receivedMsgCount = new AtomicInteger(0);

    @Override
    public void onMessage(Message message) {
        receivedMsgCount.incrementAndGet();
    }

    public int getReceivedMsgCount() {
        return receivedMsgCount.get();
    }
}
