package com.huawei.sdn.commons.selector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Session;

import org.apache.activemq.broker.BrokerService;
import org.springframework.beans.factory.annotation.Autowired;

import com.huawei.sdn.commons.jms.OVSStatsConsumer;
import com.huawei.sdn.commons.topology.manager.SwitchConfigManager;

public class QueueStatsConsumerTest {
    BrokerService broker;
    Connection connection;
    Session session;
    Destination destination;
    OVSStatsConsumer ovsStatsConsumer;

    @Autowired
    private SwitchConfigManager configManager;

/*    @Before
    public void startConfBroker() throws Exception {
        // set the configuration
//        configManager = new SwitchConfigManagerImpl();
        ovsStatsConsumer = new OVSStatsConsumer();
//        ovsStatsConsumer.switchConfigManager = configManager;
        Properties properties = new Properties();
        properties.setProperty(PropertyKeys.JMS_HOST.alias(), "localhost");
        properties.setProperty(PropertyKeys.JMS_PORT.alias(), "61616");
        configManager.setSystemProperties(new SystemProperties(properties));
        // launch the JMS broker
        broker = new BrokerService();
        TransportConnector connector = new TransportConnector();
        connector.setUri(new URI("tcp://localhost:61616"));
        broker.addConnector(connector);
        broker.setRestartAllowed(true);
        broker.setUseJmx(false);
        broker.setPersistent(false);
        broker.start();
        // create the client connection
        final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        // Create a Connection
        connection = connectionFactory.createConnection();
        connection.start();

        connection.setExceptionListener(new ExceptionListener() {
            @Override
            public void onException(JMSException exception) {
            }
        });

        // Create a Session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create the destination (Topic or Queue)
        destination = session.createTopic(TopicNames.OVS_STATS_TOPIC);

        // start the listener
        ovsStatsConsumer.start();
    }

    @After
    public void stopBroker() throws Exception {
        session.close();
        connection.close();
        ovsStatsConsumer.stop();
        broker.stop();
    }

    private String convertStreamToString(InputStream is) {
        Scanner scanner = new java.util.Scanner(is);
        try {
            java.util.Scanner s = scanner.useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        } finally {
            scanner.close();
        }
    }

//    @Test
    public void testSendReceived() throws Exception {
        // send a sample message
        final TextMessage textMessage = session.createTextMessage();
        textMessage
                .setText(convertStreamToString(this.getClass().getClassLoader().getResourceAsStream("ovsStats.json")));
        MessageProducer messageProducer = session.createProducer(destination);
        messageProducer.send(textMessage);
        // check that the message is received an well formed
        List<OFMetricsPerPort> ovsMetrics = null;
        int retry = 0;
        while (ovsMetrics == null && retry < 10) {
            ovsMetrics = ovsStatsConsumer.getLastOVSMetrics();
            retry++;
            Thread.sleep(500);
        }
        assertNotNull(ovsMetrics);
        assertEquals(ovsMetrics.size(), 6);
    } */
}
