package com.huawei.sdn.pathselector.ccm;

import javax.jms.*;

import com.huawei.sdn.commons.jms.JmsConsts;
import com.huawei.sdn.commons.selector.PathSelectorEngine;
import com.huawei.sdn.commons.selector.flow.FlowStatusHandler;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class waits on the CCM Topic, update the list of connector and remove
 * the existing flow if necessary
 */
@Component("ccmConsumer")
public class CCMConsumer implements MessageListener, ExceptionListener {//extends AbstractJmsConsumer {

    private static Logger logger = LoggerFactory.getLogger(CCMConsumer.class);

    @Autowired
    private FlowStatusHandler flowStatusHandler;

    public CCMConsumer() {
    }


    /**
     * This is the callback for handling incoming CCM messages.
     * The message contains the following params:
     *
     * linkId->port id
     * connected->isConnected
     *
     * @param message
     */
    @Override
    public void onMessage(Message message) {
        try {
            logger.debug("received message: " + message);
            final String linkId = message.getStringProperty(JmsConsts.ADDRESS_KEY);
            final boolean connected = message.getBooleanProperty(JmsConsts.CONNECTED_KEY);
            flowStatusHandler.linkStatusChanged(linkId, connected);
        } catch (JMSException e) {
            logger.warn("Caught exception reading CCM message", e);
        }
    }

    @Override
    public void onException(JMSException e) {
        logger.warn("Caught exception!!!", e);
    }
}