package com.huawei.sdn.pathselector.jms;

import java.util.List;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.SimpleType;

import com.huawei.sdn.commons.data.OFMetricsPerQueue;

public class SendJMSMessage {

    private static void receive(String srv) throws Exception {
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
        final Destination destination = session.createTopic("QUEUE_STATS");

        // Create a MessageConsumer from the Session to the Topic or Queue
        final MessageConsumer consumer = session.createConsumer(destination);
        Message message = null;
        do {
            message = consumer.receive();
            if (message instanceof TextMessage) {
                final String text = ((TextMessage) message).getText();
                final ObjectMapper mapper = new ObjectMapper();
                List<OFMetricsPerQueue> stats = mapper.readValue(text,
                        CollectionType.construct(List.class, SimpleType.construct(OFMetricsPerQueue.class)));
                System.out.println(stats);

            }
        } while (message != null);
        consumer.close();
        session.close();
        connection.close();
    }

    public static void main(String[] args) throws Exception {
        receive("tcp://localhost:61616");
    }
}
