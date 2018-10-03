package com.huawei.sdn.pathselector.jms;

import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by root on 8/4/14.
 */
@Component
public class TestCCMConsumer implements MessageListener {

    private Set<Integer> receivedMessages = new HashSet<Integer>();

    @Override
    public void onMessage(Message message) {
        MapMessage mapMessage = (MapMessage) message;
        try {
            receivedMessages.add(Integer.decode(mapMessage.getStringProperty("id")));
            System.out.println(System.currentTimeMillis() +  " TestCCMConsumer mock received message " + Integer.decode(mapMessage.getStringProperty("id")));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public Set<Integer> getReceivedMessages() {
        return receivedMessages;
    }
}
