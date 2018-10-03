package com.huawei.sdn.pathselector.jms;

import com.huawei.sdn.commons.jms.JmsConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by root on 7/3/14.
 */
@Component
public class CCMPublisher {
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Topic ccmTopic;
    private AtomicInteger msgId = new AtomicInteger(0);


    public void changeStatus(final String linkId, final boolean isConnected) {
        this.jmsTemplate.send(this.ccmTopic, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                MapMessage mm = session.createMapMessage();
                mm.setBooleanProperty(JmsConsts.CONNECTED_KEY, isConnected);
                mm.setStringProperty(JmsConsts.ADDRESS_KEY, linkId);
                mm.setIntProperty("id", msgId.getAndIncrement());
                return mm;
            }
        });
    }

}
