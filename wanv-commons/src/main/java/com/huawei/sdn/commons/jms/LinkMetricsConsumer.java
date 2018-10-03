package com.huawei.sdn.commons.jms;

import java.io.IOException;
import java.util.List;

import javax.jms.*;

import com.huawei.sdn.commons.selector.PathSelectorEngine;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.SimpleType;

import com.huawei.sdn.commons.consts.TopicNames;
import com.huawei.sdn.commons.data.CurrentLinkMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

/**
 * Created with IntelliJ IDEA. User: oWX212574 Date: 6/1/14 Time: 5:27 PM To
 * change this template use File | Settings | File Templates.
 */
//@Component("linkMetricsConsumer")
public class LinkMetricsConsumer implements MessageListener, ExceptionListener { //extends AbstractJmsConsumer {

    private final static Logger logger = LoggerFactory.getLogger(LinkMetricsConsumer.class);
    @Autowired
    protected PathSelectorEngine pathSelectorEngine;
    private final ObjectMapper mapper;

    public LinkMetricsConsumer() {
        mapper = new ObjectMapper();
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            final TextMessage textMessage = (TextMessage) message;
            try {
                final List<CurrentLinkMetrics> currentLinkMetricsList = mapper.readValue(textMessage.getText(),
                        CollectionType.construct(List.class, SimpleType.construct(CurrentLinkMetrics.class)));
                if (currentLinkMetricsList != null && currentLinkMetricsList.size() > 0) {

                    pathSelectorEngine.metricsReceived(currentLinkMetricsList);

                }
            }
            catch (Exception e) {
                logger.warn("Caught Exception", e);
            }
        } else {
            throw new RuntimeException("No text Message is received...");
        }
    }


    @Override
    public void onException(JMSException e) {
        logger.warn("Exception!", e);
    }
}
