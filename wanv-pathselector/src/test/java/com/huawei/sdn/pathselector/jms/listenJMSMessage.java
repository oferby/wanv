package com.huawei.sdn.pathselector.jms;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;

import com.huawei.sdn.commons.jms.JmsConsts;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.huawei.sdn.pathselector.ccm.CCMConsumer;

public class listenJMSMessage {

    private static void send(String srv, String ip, boolean connected) throws Exception {
        final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(srv);

        // Create a Connection
        final Connection connection = connectionFactory.createConnection();
        connection.start();

        connection.setExceptionListener(new ExceptionListener() {
            @Override
            public void onException(JMSException exception) {
            }
        });

        // Create a Session
        final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create the destination (Topic or Queue)
        final Destination destination = session.createTopic("CCM");

        // Create a MessageConsumer from the Session to the Topic or Queue
        final MessageProducer producer = session.createProducer(destination);
        final MapMessage mapMessage = session.createMapMessage();
        mapMessage.setBooleanProperty(JmsConsts.CONNECTED_KEY, connected);
        mapMessage.setStringProperty(JmsConsts.ADDRESS_KEY, ip);
        producer.send(mapMessage);
        producer.close();
        session.close();
        connection.close();
    }

    public static void main(String[] args) throws Exception {
        send("tcp://localhost:61616", "10.1.80.85", true);
        send("tcp://localhost:61617", "10.1.80.80", true);
    }
}
