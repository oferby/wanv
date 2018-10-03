package com.huawei.sdn.commons.jms;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import com.huawei.sdn.commons.selector.PathSelectorEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractJmsConsumer implements Runnable, ExceptionListener {
    public static final int RETRY_TIMEOUT = 30000;
    public static final int RECEIVE_TIMEOUT = 1000;
    private boolean active, stopped;
    private String topicName;
    private Thread thread;
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractJmsConsumer.class);

//    @Autowired
//    protected SwitchConfigManager switchConfigManager;

    @Autowired
    protected PathSelectorEngine pathSelectorEngine;

    public AbstractJmsConsumer(String topicName) {
        super();
        active = true;
        this.topicName = topicName;
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        setActive(false);
        thread.interrupt();
        try {
            while (!isStopped()) {
                synchronized (this) {
                    Thread.sleep(AbstractJmsConsumer.RECEIVE_TIMEOUT / 2);
                }
            }
        } catch (final InterruptedException e) {
            LOGGER.error("Error when waiting for " + topicName + " is stopped.", e);
        }
        thread = null;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isStopped() {
        return stopped;
    }

    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    /**
     * connect to the JMS server and create the topic client
     *
     * @throws JMSException
     *             on connection error
     */
    private void connect() throws JMSException {

        // Create a ConnectionFactory
//        final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://"
//                + switchConfigManager.getSystemProperties().getJMSHost().trim() + ":"
//                + switchConfigManager.getSystemProperties().getJMSPort());

        // Create a Connection
//        connection = connectionFactory.createConnection();
//        connection.start();

//        connection.setExceptionListener(this);

        // Create a Session
//        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create the destination (Topic or Queue)
//        final Destination destination = session.createTopic(topicName);

        // Create a MessageConsumer from the Session to the Topic or Queue
//        consumer = session.createConsumer(destination);
    }

    /**
     * disconnect from the JMS server
     */
    private void disconnect() {
        try {
            if (consumer != null) {
                consumer.close();
            }
        } catch (final Exception e) {
            LOGGER.error("error during consumer close");
        }
        try {
            if (session != null) {
                session.close();
            }
        } catch (final Exception e) {
            LOGGER.error("error during session close");
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (final Exception e) {
            LOGGER.error("error during connection close");
        }
    }

    @Override
    public void run() {
        LOGGER.info("Started.");
        stopped = false;
        try {
            while (active) {
                try {
                    // try to connect
                    connect();
                    while (active) {
                        // Wait for a message 30s
                        final Message message = consumer.receive(RECEIVE_TIMEOUT);
                        if (message != null) {
                            messageReceived(message);
                        } else {
                            LOGGER.trace("No Message");
                        }
                    }
                } catch (final Exception e) {
                    if (e instanceof InterruptedException || e.getCause() instanceof InterruptedException) {
                        LOGGER.info("the thread is interrupted");
                    } else {
                        LOGGER.error("error during receiving message. ", e);
                        // disconnect and wait 10s to retry
                        disconnect();
                        try {
                            Thread.sleep(RETRY_TIMEOUT);
                        } catch (final InterruptedException e1) {
                            active = false;
                        }
                    }
                }
            }
        } finally {
            disconnect();
            LOGGER.info("Stopped.");
            stopped = true;
        }
    }

    /**
     * Handle A Link Change
     *
     */
    public abstract void messageReceived(final Message message) throws JMSException;

    @Override
    public synchronized void onException(JMSException ex) {
        LOGGER.error("JMS Exception occured.  Shutting down client.", ex);
    }
}
