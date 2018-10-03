package com.huawei.sdn.pathselector.jms;

import com.huawei.sdn.commons.jms.LinkMetricsConsumer;
import com.huawei.sdn.commons.selector.PathSelectorEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by root on 7/3/14.
 */
@Configuration
@ComponentScan("com.huawei.sdn.pathselector.ccm")
public class JMSContext {

    @Bean
    public PathSelectorEngine getPathSelectorEngine() {
        return new PathSelectorEngineMock();
    }

    @Bean
    public LinkMetricsConsumer geLinkMetricsConsumer() {
        return new LinkMetricsConsumer();
    }
}
