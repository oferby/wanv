package com.huawei.sdn.commons.jms;

import com.huawei.sdn.commons.data.OFMetricsPerPort;
import com.huawei.sdn.commons.selector.PathSelectorEngine;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.SimpleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: oWX212574 Date: 6/1/14 Time: 5:27 PM To
 * change this template use File | Settings | File Templates.
 */
//@Component("ovsStatsConsumer")
public class OVSStatsConsumer implements MessageListener, ExceptionListener {

    private final static Logger logger = LoggerFactory.getLogger(OVSStatsConsumer.class);
    private final ObjectMapper mapper;
    private List<OFMetricsPerPort> lastOVSMetrics;
    @Autowired
    protected PathSelectorEngine pathSelectorEngine;

    public OVSStatsConsumer() {
//        super(TopicNames.OVS_STATS_TOPIC);
        mapper = new ObjectMapper();
    }

//    @Override
//    public void messageReceived(Message message) throws JMSException {
//        if (message instanceof TextMessage) {
//            final TextMessage textMessage = (TextMessage) message;
//            try {
//                System.out.println(textMessage.getText());
//                final List<OFMetricsPerPort> ovsMetrics = mapper.readValue(textMessage.getText(),
//                        CollectionType.construct(List.class, SimpleType.construct(OFMetricsPerPort.class)));
//                if (ovsMetrics != null && ovsMetrics.size() > 0) {
//                    lastOVSMetrics = ovsMetrics;
//                    pathSelectorEngine.metricsOVSReceived(ovsMetrics);
//                }
//            } catch (final IOException e) {
//                throw new RuntimeException(e);
//            }
//        } else {
//            throw new RuntimeException("No text Message is received...");
//        }
//    }

    public List<OFMetricsPerPort> getLastOVSMetrics() {
        return lastOVSMetrics;
    }

    @Override
    public void onException(JMSException e) {
        logger.warn("Caught exception!", e);
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            final TextMessage textMessage = (TextMessage) message;
            try {
                System.out.println(textMessage.getText());
                final List<OFMetricsPerPort> ovsMetrics = mapper.readValue(textMessage.getText(),
                        CollectionType.construct(List.class, SimpleType.construct(OFMetricsPerPort.class)));
                if (ovsMetrics != null && ovsMetrics.size() > 0) {
                    lastOVSMetrics = ovsMetrics;
                    pathSelectorEngine.metricsOVSReceived(ovsMetrics);
                }
            } catch (final Exception e) {
                logger.warn("Caught exception!", e);
            }
        } else {
            throw new RuntimeException("No text Message is received...");
        }

    }
}
