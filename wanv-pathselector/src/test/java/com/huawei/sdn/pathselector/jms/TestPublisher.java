package com.huawei.sdn.pathselector.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;

/**
 * Created by root on 8/4/14.
 */
@Component
public class TestPublisher {

    private int msgId = 0;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Topic testTopic;

    public void sendMessage() {
        jmsTemplate.send(this.testTopic, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage();
                textMessage.setText(Integer.toString(msgId));
                textMessage.setIntProperty("id", msgId);
                msgId++;
                return textMessage;
            }
        });
    }
}
