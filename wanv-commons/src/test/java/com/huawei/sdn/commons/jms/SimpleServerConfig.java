package com.huawei.sdn.commons.jms;

import com.huawei.sdn.commons.selector.flow.FlowStatusHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/28/14
 * Time: 7:25 PM
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class SimpleServerConfig {


    @Bean
    public FlowStatusHandler getFlowStatusHandler(){
        return new FlowStatusHandlerMock();
    }

    @Bean
    public SimpleServer getSimpleServer(){
        return new SimpleServer();
    }

}
