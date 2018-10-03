package com.huawei.sdn.pathselector.jms;

import com.huawei.sdn.commons.selector.flow.FlowStatusHandler;
import com.huawei.sdn.pathselector.mock.jms.FlowStatusHandlerMock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/28/14
 * Time: 9:21 AM
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class JmsTestConfig {

    @Bean
    public FlowStatusHandler getFlowStatusHandler(){

        return new FlowStatusHandlerMock();

    }


}
